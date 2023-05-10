package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.User;
import com.atibusinessgroup.amanyaman.repository.UserRepository;
import com.atibusinessgroup.amanyaman.security.UserLockedException;
import com.atibusinessgroup.amanyaman.service.UserService;
import com.atibusinessgroup.amanyaman.util.JWTUtil;
import com.atibusinessgroup.amanyaman.web.rest.errors.UnlockedException;
import com.atibusinessgroup.amanyaman.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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

    // public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, DomainUserDetailsService domainUserDetailsService, AuthenticationProvider domainAuthenticationProvider, PolicyUserDetailsService policyUserDetailsService, UserService userService, UserRepository userRepository) {
    //     this.tokenProvider = tokenProvider;
    //     this.authenticationManagerBuilder = authenticationManagerBuilder;
    //     this.domainUserDetailsService = domainUserDetailsService;
    //     this.domainAuthenticationProvider = domainAuthenticationProvider;
    //     this.policyUserDetailsService = policyUserDetailsService;
    //     this.userService = userService;
    //     this.userRepository = userRepository;
    // }

    @PostMapping("/api/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
//        UsernamePasswordAuthenticationToken authenticationToken =
//            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // UserAmanyaman userDetails = (UserAmanyaman) domainUserDetailsService.loadUserByUsername(loginVM.getUsername());
        // TokenBasedAuthentication tokenBasedAuthentication = new TokenBasedAuthentication(userDetails, loginVM.getPassword(), userDetails.getAuthorities());

        String login = loginVM.getUsername().toLowerCase(Locale.ENGLISH);
        User u = null;
        if (new EmailValidator().isValid(loginVM.getUsername(), null)) {
            u = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login).get();
        }
//        else{
//            u = userRepository.findOneWithAuthoritiesByLogin(login).get();
//        }

        try {
            // Authentication authentication = domainAuthenticationProvider.authenticate(tokenBasedAuthentication);
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();

            Map<String, Object> claims = new HashMap<>();
            // claims.put(USER_TYPE_KEY, UserAmanyaman.UserAmamyamanType.JHIPSTER.name());
            // claims.put(USER_LASTNAME_KEY, userDetails.getLastName());
            // claims.put(USER_FIRSTNAME_KEY, userDetails.getFirstName());
            Optional<User> user = userService.getUserWithAuthoritiesByLogin(u.getLogin());
            // if(user.isPresent()){
            //     if(user.get().getTravelAgent() != null){
            //         if(user.get().getTravelAgent().getId() != null){
            //             claims.put(USER_TRAVEL_AGENT, user.get().getTravelAgent().getId());
            //         }
            //     }
            //     claims.put(USER_TRAVEL_AGENT_STAFF, user.get().getId());
            // }
            // String jwt = tokenProvider.createToken(authentication, rememberMe, claims);
            String jwt = jwtUtil.generateToken();
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
                    null, 
                    null, 
                    "Admin",  
                    "Test", 
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
