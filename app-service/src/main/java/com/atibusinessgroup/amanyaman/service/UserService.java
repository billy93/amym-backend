package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.config.Constants;
import com.atibusinessgroup.amanyaman.domain.Authority;
import com.atibusinessgroup.amanyaman.domain.User;
import com.atibusinessgroup.amanyaman.repository.AuthorityRepository;
import com.atibusinessgroup.amanyaman.repository.UserRepository;
import com.atibusinessgroup.amanyaman.security.SecurityUtils;
import com.atibusinessgroup.amanyaman.service.dto.TravelAgentDTO;
import com.atibusinessgroup.amanyaman.service.dto.UserDTO;
import com.atibusinessgroup.amanyaman.service.dto.UserTravelAgentDTO;
import com.atibusinessgroup.amanyaman.util.AuthoritiesConstants;
import com.atibusinessgroup.amanyaman.util.RandomUtil;
import com.atibusinessgroup.amanyaman.web.rest.dto.UserSearchRequestDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    // private final CacheManager cacheManager;

    private final TravelAgentService travelAgentService;

     private final MailService mailService;

    public static final int MAX_FAILED_ATTEMPTS = 3;
    public static final long LOCK_TIME_DURATION = 1 * 60 * 1000;
//    public static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
                       TravelAgentService travelAgentService, MailService mailService
    // , CacheManager cacheManager
    // , TravelAgentService travelAgentService, MailService mailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        // this.cacheManager = cacheManager;
         this.travelAgentService = travelAgentService;
         this.mailService = mailService;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(18000)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                user.setLockTime(null);
                user.setFailedAttempt(0);
                user.setAccountNonLocked(true);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                // throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                // throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserTravelAgentDTO> updateUserTravelAgent(UserTravelAgentDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if(userDTO.getTravelAgent() != null) {
                        if(userDTO.getTravelAgent().getId() != null) {
                            user.setTravelAgent(travelAgentService.findOne(userDTO.getTravelAgent().getId()).get());
                        }
                    } else {
                        user.setTravelAgent(null);
                    }
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO.getAuthorities().stream()
                            .map(authorityRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(managedAuthorities::add);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(UserTravelAgentDTO::new);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    public User createUser(UserTravelAgentDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if(userDTO.getTravelAgent() != null) {
            if(userDTO.getTravelAgent().getId() != null) {
                user.setTravelAgent(travelAgentService.findOne(userDTO.getTravelAgent().getId()).get());
            }
        } else {
            user.setTravelAgent(null);
        }
//        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        String encryptedPassword = passwordEncoder.encode("abcd1234");
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String id) {
        userRepository.findById(Long.parseLong(id)).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }


    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthoritiesByLogin(Long id) {
       return userRepository.findOneWithAuthoritiesById(id);
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthoritiesById(Long id) {
       return userRepository.findOneWithAuthoritiesById(id);
   }

    // @Transactional(readOnly = true)
    // public Optional<User> getUserWithAuthorities() {
    //     return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    // }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByEmail(String email) {
        return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(email);
    }
    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public void createAllUser(List<UserTravelAgentDTO> userList) {
        for(UserTravelAgentDTO u : userList){
            try {
                User x = createUser(u);
                mailService.sendCreationEmail(x);
            } catch(Exception e) {
                System.out.println("ERROR "+e.getMessage());
            }
        }
    }


    public User increaseFailedAttempt(User u) {
        userRepository.updateFailedAttempt(u.getFailedAttempt()+1, u.getLogin());
        return getUserWithAuthoritiesByLogin(u.getLogin()).get();
    }

    public void lock(User u) {
        u.setAccountNonLocked(false);
        u.setLockTime(Instant.now());
        userRepository.save(u);
    }

    public boolean unlock(User u){
        long lockTimeInMillis = u.getLockTime().toEpochMilli();
        long currentTimeMillis = System.currentTimeMillis();

        if(lockTimeInMillis + LOCK_TIME_DURATION < currentTimeMillis){
            u.setAccountNonLocked(true);
            u.setLockTime(null);
            u.setFailedAttempt(0);
            userRepository.save(u);
            return true;
        }

        return false;
    }

    public boolean forceUnlock(User u){
        u.setAccountNonLocked(true);
        u.setLockTime(null);
        u.setFailedAttempt(0);
        userRepository.save(u);
        return true;
    }

    public void resetFailedAttempts(User u) {
        userRepository.updateFailedAttempt(0, u.getLogin());
    }

    public boolean checkPassword(User u, String password){
        if(passwordEncoder.matches(password, u.getPassword())){
            return true;
        }
        return false;
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void updateUserLastLatestFeed(long userId){
        userRepository.updateUserLastLatestFeed(userId);
    }

    @Transactional(readOnly = true)
    public Page<UserTravelAgentDTO> getAllManagedUsers(Pageable pageable, UserSearchRequestDTO userSearchRequestDTO) {
        return userRepository.findAllBy(userSearchRequestDTO, pageable).map(UserTravelAgentDTO::new);
    }

}
