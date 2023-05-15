package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.User;
// import com.atibusinessgroup.amanyaman.module.user.policy.client.UserPolicyServiceProxy;
import com.atibusinessgroup.amanyaman.repository.UserRepository;
import com.atibusinessgroup.amanyaman.security.SecurityUtils;
// import com.atibusinessgroup.amanyaman.security.UserAmanyaman;
import com.atibusinessgroup.amanyaman.service.MailService;
import com.atibusinessgroup.amanyaman.service.UserService;
// import com.atibusinessgroup.amanyaman.service.dto.PasswordChangeDTO;
import com.atibusinessgroup.amanyaman.service.dto.UserDTO;
import com.atibusinessgroup.amanyaman.web.rest.dto.KeyAndPasswordDTO;
import com.atibusinessgroup.amanyaman.web.rest.dto.ManagedUserDTO;
import com.atibusinessgroup.amanyaman.web.rest.dto.PasswordResetRequestDTO;
import com.atibusinessgroup.amanyaman.web.rest.errors.*;
// import com.atibusinessgroup.amanyaman.web.rest.vm.KeyAndPasswordVM;
// import com.atibusinessgroup.amanyaman.web.rest.vm.ManagedUserVM;

// import com.atibusinessgroup.amanyaman.module.user.policy.query.FindOneUserPolicyByPolicyNumberFirstNameAndLastName;
// import com.atibusinessgroup.amanyaman.module.user.policy.query.dto.UserPolicyDTO;
import org.apache.commons.lang3.StringUtils;
// import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/app")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    // private final UserPolicyServiceProxy userPolicyServiceProxy;
//    private final QueryGateway queryGateway;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService
    // , UserPolicyServiceProxy userPolicyServiceProxy
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        // this.userPolicyServiceProxy = userPolicyServiceProxy;
//        this.queryGateway = queryGateway;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }
    */

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     
    @GetMapping("/account")
    public UserDTO getAccount() {
        System.out.println("Authenticated : "+SecurityUtils.isAuthenticated());
        System.out.println("Authentication provider : "+SecurityUtils.getAuthenticationProvider());
//        System.out.println("Current login : "+SecurityUtils.getCurrentUserLogin().get());
        if(SecurityUtils.getAuthenticationProvider() == UserAmanyaman.UserAmamyamanType.JHIPSTER) {
            return userService.getUserWithAuthorities()
                .map(UserDTO::new)
                .orElseThrow(() -> new AccountResourceException("User could not be found"));
        }
        else if(SecurityUtils.getAuthenticationProvider() == UserAmanyaman.UserAmamyamanType.POLICY){

            UserPolicyDTO userPolicyDTO = userPolicyServiceProxy.find(SecurityUtils.getUserPolicy().getUsername(),
                SecurityUtils.getUserPolicy().getFirstName(),
                SecurityUtils.getUserPolicy().getLastName()).getBody();

//            UserPolicyDTO userPolicyDTO = queryGateway.query(
//                new FindOneUserPolicyByPolicyNumberFirstNameAndLastName(
//                    SecurityUtils.getUserPolicy().getUsername(),
//                    SecurityUtils.getUserPolicy().getFirstName(),
//                    SecurityUtils.getUserPolicy().getLastName()), UserPolicyDTO.class).get();
            if(userPolicyDTO != null) {
                UserDTO dummy = new UserDTO();
                Set<String> authorities = new HashSet<>();
                authorities.add("ROLE_USER");
                dummy.setLogin(userPolicyDTO.getPolicyNumber());
                dummy.setLangKey("en");
                dummy.setFirstName(userPolicyDTO.getFirstName());
                dummy.setLastName(userPolicyDTO.getLastName());
                dummy.setActivated(true);
                dummy.setAuthorities(authorities);
                return dummy;
            }
            throw new AccountResourceException("User could not be found");
        } else {
            throw new AccountResourceException("User could not be found");
        }
    }
    */
    
    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }
    */

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
    
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }
    */
    
    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody PasswordResetRequestDTO mail) {
        Optional<User> user = userService.requestPasswordReset(mail.getEmail());
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/claim/init")
    public void requestPasswordResetClaim(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetClaimMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
    */
    @PostMapping(path = "/account/reset-password/finish")
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }

        if(keyAndPassword.getNewPassword().contentEquals(keyAndPassword.getPasswordRetype())){
            Optional<User> user =
                userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

            if (!user.isPresent()) {
                throw new AccountResourceException("No user was found for this reset key");
            }

            mailService.sendFinishPasswordReset(user.get());
            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.badRequest().build();
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserDTO.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserDTO.PASSWORD_MAX_LENGTH;
    }
}
