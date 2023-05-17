package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.config.Constants;
import com.atibusinessgroup.amanyaman.domain.Authority;
import com.atibusinessgroup.amanyaman.domain.TravelAgent;
// import com.atibusinessgroup.amanyaman.domain.TravelAgent;
import com.atibusinessgroup.amanyaman.domain.User;
// import com.atibusinessgroup.amanyaman.module.master.user.query.dto.UserCriteria;
// import com.atibusinessgroup.amanyaman.module.sales.travelinsurance.dto.PolicyListDataDTO;
import com.atibusinessgroup.amanyaman.repository.UserRepository;
import com.atibusinessgroup.amanyaman.security.AuthoritiesConstants;
import com.atibusinessgroup.amanyaman.security.SecurityUtils;
import com.atibusinessgroup.amanyaman.service.*;
import com.atibusinessgroup.amanyaman.service.dto.UserDTO;
import com.atibusinessgroup.amanyaman.service.dto.UserTravelAgentDTO;
import com.atibusinessgroup.amanyaman.util.HeaderUtil;
import com.atibusinessgroup.amanyaman.util.PaginationUtil;
import com.atibusinessgroup.amanyaman.util.ResponseUtil;
import com.atibusinessgroup.amanyaman.web.rest.dto.UserSearchRequestDTO;
// import com.atibusinessgroup.amanyaman.service.dto.UserTravelAgentDTO;
import com.atibusinessgroup.amanyaman.web.rest.errors.BadRequestAlertException;
import com.atibusinessgroup.amanyaman.web.rest.errors.EmailAlreadyUsedException;
import com.atibusinessgroup.amanyaman.web.rest.errors.LoginAlreadyUsedException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.CellType;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.xssf.usermodel.XSSFSheet;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

// import static com.atibusinessgroup.amanyaman.module.util.TokenKeyConstant.USER_TRAVEL_AGENT;
// import static com.atibusinessgroup.amanyaman.module.util.TokenKeyConstant.USER_TRAVEL_AGENT_STAFF;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api/app")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private String applicationName = "AMANYAMAN";

    private final UserService userService;
    private final UserRepository userRepository;

    private final MailService mailService;
    private final ExportService exportService;
    // private final TravelAgentService travelAgentService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService, 
    ExportService exportService
    //, TravelAgentService travelAgentService
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        // this.userQueryService = userQueryService;
        this.exportService = exportService;
        // this.travelAgentService = travelAgentService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code POST  /users/travel-agent}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
    
    @PostMapping("/users/travel-agent")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUserTravelAgent(@Valid @RequestBody UserTravelAgentDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUserTravelAgent(userDTO);
//            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }
 */
    /**
     * {@code POST  /users/create}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
    
    @PostMapping("/users/create")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUserAmanyaman(@Valid @RequestBody UserTravelAgentDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User Amanyaman: {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUserAmanyaman(userDTO);
            mailService.sendCreationEmail(newUser);

            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }
 */
    /**
     * {@code PUT  /users/update}  : Update a user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
    
    @PutMapping("/users/update")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUserAmanyaman(@Valid @RequestBody UserTravelAgentDTO userDTO) throws URISyntaxException {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUserTravelAgent(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }
 */
    public static class UserLatestFeed{
        private long userId;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }
    /**
     * {@code PUT  /users/update-latest-feed}  : Update a user latest feed.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param user the user to update.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PutMapping("/users/update-latest-feed")
    public ResponseEntity<Void> updateUserLastLatestFeed(@RequestBody UserLatestFeed user) throws URISyntaxException {
        userService.updateUserLastLatestFeed(user.getUserId());
        return ResponseEntity.ok().build();
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     
    @PutMapping("/users/travel-agent")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUserTravelAgent(@Valid @RequestBody UserTravelAgentDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUserTravelAgent(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    @PutMapping("/users/unlock/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<Void> unlockUser(@PathVariable String login){
        Optional<User> userOptional = userService.getUserWithAuthoritiesByLogin(login);
        if(userOptional.isPresent()) {
            User u = userOptional.get();
            userService.forceUnlock(u);
        }
        return ResponseEntity.ok().build();
    }*/

    @PutMapping("/users/lock/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<Void> lockUser(@PathVariable String login){
        Optional<User> userOptional = userService.getUserWithAuthoritiesByLogin(login);
        if(userOptional.isPresent()) {
            User u = userOptional.get();
            userService.lock(u);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
    */ 
    @GetMapping("/users")
    public ResponseEntity<List<UserTravelAgentDTO>> getAllUsers(UserSearchRequestDTO userSearchRequestDTO, Pageable pageable) {
        final Page<UserTravelAgentDTO> page = userService.getAllManagedUsers(pageable, userSearchRequestDTO);        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /users/search} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
    @GetMapping("/users/search")
    public ResponseEntity<List<UserTravelAgentDTO>> getAllUsersSearch(UserCriteria criteria, Pageable pageable) {
        final Page<User> page = userQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<List<UserTravelAgentDTO>>(page.getContent().stream().map(e -> {
            UserTravelAgentDTO uta = new UserTravelAgentDTO();
            uta.setId(e.getId());

            if(e.getTravelAgent() != null) {
                TravelAgentDTO dto = new TravelAgentDTO();
                dto.setId(e.getTravelAgent().getId());
                dto.setTravelAgentName(e.getTravelAgent().getTravelAgentName());
                uta.setTravelAgent(dto);
            }

            Set<Authority> authoritySet = e.getAuthorities();
            Set<String> authorities = new HashSet<>();
            authoritySet.stream().forEach(f ->
                authorities.add(f.getName()));
            uta.setAuthorities(authorities);

            uta.setActivated(e.isActivated());
            uta.setCreatedBy(e.getCreatedBy());
            uta.setEmail(e.getEmail());
            uta.setCreatedDate(e.getCreatedDate());
            uta.setFirstName(e.getFirstName());
            uta.setLastName(e.getLastName());
            uta.setLogin(e.getLogin());
            return uta;
        }).collect(Collectors.toList()), headers, HttpStatus.OK);
    }
     */

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param id the id of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
    
    @GetMapping("/users/getById/{id}")
    public ResponseEntity<UserTravelAgentDTO> getUserById(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesById(id)
                .map(UserTravelAgentDTO::new));
    } */

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param id the id of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
    */
    @GetMapping("/users/getByEmail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        log.debug("REST request to get User : {}", email);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByEmail(email)
                .map(UserDTO::new));
    }


    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     
    @GetMapping("/users/travel-agent/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserTravelAgentDTO> getUserTravelAgent(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserTravelAgentDTO::new));
    }*/

    /**
     * {@code GET /users/:id} : get the "id" user.
     *
     * @param id the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
//    @GetMapping("/users/travel-agent/byId/{id}")
//    public ResponseEntity<UserTravelAgentDTO> getUserTravelAgent(@PathVariable Long id) {
//        log.debug("REST request to get User : {}", id);
//        return ResponseUtil.wrapOrNotFound(
//            userService.getUserWithAuthoritiesById(id)
//                .map(UserTravelAgentDTO::new));
//    }


    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')" +
        " || hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "A user is deleted with identifier " + login, login)).build();
    }

    @PostMapping("/users/list/export")
    public ResponseEntity<Resource> exportUserList(Pageable page) throws IOException {

        Page<User> userPage = userRepository.findAll(page);
        Resource file = exportService.exportUsers(userPage.toList());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"Users.xlsx\"")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(file.contentLength())
            .body(file);
    }

    /*   
    @ResponseStatus(OK)
    @PostMapping("/users/list/import")
    public ResponseEntity<List<UserTravelAgentDTO>> importExcelFile(@RequestParam("file") MultipartFile files) throws IOException {
        HttpStatus status = HttpStatus.OK;
        List<UserTravelAgentDTO> userList = new ArrayList<>();

        Pageable p = PageRequest.of(0, 9999);
        Page<TravelAgent> travelAgents = travelAgentService.findAll(p);
        List<TravelAgent> travelAgentList = travelAgents.getContent();

        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        int index = 0;
        for (Row row: worksheet) {
            if (index > 0) {
                UserTravelAgentDTO user = new UserTravelAgentDTO();
                if(!checkIfRowIsEmpty(row)) {
                    user.setLogin(UUID.randomUUID().toString());
                    try {
                        user.setFirstName(row.getCell(0).getStringCellValue());
                    } catch (Exception e) {

                    }

                    try {
                        user.setLastName(row.getCell(1).getStringCellValue());
                    } catch (Exception e) {

                    }

                    try {
                        user.setEmail(row.getCell(2).getStringCellValue());
                    } catch (Exception e) {

                    }

                    try {
                        Set<String> authoritySet = new HashSet<>();
                        authoritySet.add(row.getCell(3).getStringCellValue());
                        user.setRoles(authoritySet);
                    } catch (Exception e) {

                    }

                    try {
                        String travelAgent = row.getCell(4).getStringCellValue();
                        Optional<TravelAgent> ta = travelAgentList.stream().filter(e -> e.getCustcode().contentEquals(travelAgent)).findFirst();
                        if(ta.isPresent()){
                            TravelAgent travelAgent1 = ta.get();
                            TravelAgentDTO travelAgentDTO = new TravelAgentDTO();
                            travelAgentDTO.setId(travelAgent1.getId());
                            travelAgentDTO.setTravelAgentName(travelAgent1.getTravelAgentName());
                            user.setTravelAgent(travelAgentDTO);
                        }
                        else{
                            System.out.println("Travel agent not found");
                        }
                    } catch (Exception e) {
                        System.out.println("Import User Get Travel Agent : "+e.getMessage());
                    }
                    userList.add(user);
                }
                else{
                    System.out.println("Empty row");
                }
            }
            index++;
        }

        if(userList.size() > 0) {
            userService.createAllUser(userList);
        }
        return ResponseEntity.ok().build();
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            cell.getCellType()
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString()) && !cell.toString().contentEquals("")) {
                return false;
            }
        }
        return true;
    }
    */
}
