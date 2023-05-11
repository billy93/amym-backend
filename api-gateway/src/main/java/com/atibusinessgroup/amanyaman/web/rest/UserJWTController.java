package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.User;
import com.atibusinessgroup.amanyaman.repository.UserRepository;
import com.atibusinessgroup.amanyaman.security.UserLockedException;
import com.atibusinessgroup.amanyaman.service.UserService;
import com.atibusinessgroup.amanyaman.util.JWTUtil;
import com.atibusinessgroup.amanyaman.web.rest.errors.UnlockedException;
import com.atibusinessgroup.amanyaman.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.discovery.converters.Auto;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

// import static com.atibusinessgroup.amanyaman.module.util.TokenKeyConstant.*;

/**
 * Controller to authenticate users.
 */
@RestController
public class UserJWTController {

    // private final TokenProvider tokenProvider;
    // private final AuthenticationManagerBuilder authenticationManagerBuilder;
    // private final DomainUserDetailsService domainUserDetailsService;
    // @Qualifier("domainAuthenticationProvider")
    // private final AuthenticationProvider domainAuthenticationProvider;
    // private final PolicyUserDetailsService policyUserDetailsService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private PasswordEncoder passwordEncoder;

    // public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, DomainUserDetailsService domainUserDetailsService, AuthenticationProvider domainAuthenticationProvider, PolicyUserDetailsService policyUserDetailsService, UserService userService, UserRepository userRepository) {
    //     this.tokenProvider = tokenProvider;
    //     this.authenticationManagerBuilder = authenticationManagerBuilder;
    //     this.domainUserDetailsService = domainUserDetailsService;
    //     this.domainAuthenticationProvider = domainAuthenticationProvider;
    //     this.policyUserDetailsService = policyUserDetailsService;
    //     this.userService = userService;
    //     this.userRepository = userRepository;
    // }

    public User loadUserByUsername(final String login) {
        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    @PostMapping("/api/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        String login = loginVM.getUsername().toLowerCase(Locale.ENGLISH);
        if (new EmailValidator().isValid(loginVM.getUsername(), null)) {
            User u = loadUserByUsername(login);

            try {                
                if (!userService.checkPassword(u, loginVM.getPassword())) {      
                    if(u.isAccountNonLocked()){
                        throw new BadCredentialsException("Bad credentials");
                    }  
                    else{
                        throw new LockedException("Your account has been locked");
                    }
                }
                else{
                    if(!u.isAccountNonLocked()){
                        throw new LockedException("Its already 1 day, now we will unlock your user");
                    }
                }

                boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
                Optional<User> user = userService.getUserWithAuthoritiesByLogin(u.getLogin());
                String jwt = jwtUtil.generateToken(user.get(), rememberMe);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
    
                if(u.isAccountNonLocked()) {
                    if (u.getFailedAttempt() > 0) {
                        userService.resetFailedAttempts(u);
                    }
                }
    
                List<String> roles = u.getAuthorities().stream()
                    .map( a -> a.getName() )
                    .collect( Collectors.toList() );
                return new ResponseEntity<>(
                    new JWTToken(
                        jwt, 
                        String.valueOf(u.getId()), 
                        null, 
                        u.getFirstName(),  
                        u.getLastName(), 
                        null,  
                        null, 
                        roles), 
                        httpHeaders, HttpStatus.OK);
            } catch(BadCredentialsException e){
                if(u.isActivated() && u.isAccountNonLocked()) {
                    if(u.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                        u = userService.increaseFailedAttempt(u);
                        throw new BadCredentialsException(String.valueOf(UserService.MAX_FAILED_ATTEMPTS - u.getFailedAttempt()));
                    }
                    else{
                        userService.lock(u);
                        throw new UserLockedException("Your account has been locked due to 3 failed attempts. It will be unlocked after 24 hours.");
                    }
                }
                throw e;
            } catch(LockedException exception){
                if(userService.checkPassword(u, loginVM.getPassword())){
                    if(userService.unlock(u)){
                        throw new UnlockedException("Your account has been unlocked, Please try to login again.");
                    }
                    else{
                        throw new LockedException("User account is locked.");
                    }
                } else{
                    throw new LockedException("User account is locked.");
                }
            }
        
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
    }

    @GetMapping("/api/test")
    public Mono<Object> test(){
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println(authentication.getPrincipal());
        // Mono<String> user = ReactiveSecurityContextHolder.getContext()
        //     .map(SecurityContext::getAuthentication)
        //     .map(Authentication::getPrincipal)
        //     .cast(String.class);
       
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .flatMap(authentication -> {
                System.out.println("Authentication object");
                System.out.println(authentication);
                return Mono.empty();
                // if (authentication == null) {
                //     return Mono.empty();
                // } else {
                //     return Mono.just(authentication.getPrincipal().toString());
                // }
            });
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;
        private String userId;
        private String travelAgentId;
        private String firstName;
        private String lastName;
        private String policyNumber;
        private String travelAgentName;
        private List<String> roles;

        JWTToken(String idToken, String userId, String travelAgentId, String firstName, String lastName, String policyNumber, String travelAgentName, List<String> roles) {
            this.idToken = idToken;
            this.userId = userId;
            this.travelAgentId = travelAgentId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.policyNumber = policyNumber;
            this.roles = roles;
            this.travelAgentName = travelAgentName;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("user_id")
        String getUserId() {
            return userId;
        }

        void setUserId(String userId) {
            this.userId = userId;
        }

        @JsonProperty("travel_agent_id")
        String getTravelAgentId() {
            return travelAgentId;
        }

        void setTravelAgentId(String travelAgentId) {
            this.travelAgentId = travelAgentId;
        }

        @JsonProperty("roles")
        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

		public String getPolicyNumber() {
			return policyNumber;
		}

		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}

        @JsonProperty("travel_agent_name")
        public String getTravelAgentName() {
            return travelAgentName;
        }

        public void setTravelAgentName(String travelAgentName) {
            this.travelAgentName = travelAgentName;
        }
    }
}
