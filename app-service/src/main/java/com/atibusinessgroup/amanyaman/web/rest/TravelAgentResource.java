package com.atibusinessgroup.amanyaman.web.rest;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.atibusinessgroup.amanyaman.util.HeaderUtil;
import com.atibusinessgroup.amanyaman.util.PaginationUtil;
import com.atibusinessgroup.amanyaman.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.atibusinessgroup.amanyaman.domain.TravelAgent;
//import com.atibusinessgroup.amanyaman.module.master.travelAgent.query.dto.TravelAgentCriteria;
//import com.atibusinessgroup.amanyaman.service.TravelAgentQueryService;
import com.atibusinessgroup.amanyaman.service.TravelAgentService;
import com.atibusinessgroup.amanyaman.web.rest.dto.TravelAgentSearchRequestDTO;
import com.atibusinessgroup.amanyaman.web.rest.errors.BadRequestAlertException;


/**
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.TravelAgent}.
 */
@RestController
@RequestMapping("/api/app")
public class TravelAgentResource {

    private final Logger log = LoggerFactory.getLogger(TravelAgentResource.class);

    private static final String ENTITY_NAME = "travelAgent";

    private String applicationName = "AMANYAMAN";

    private final TravelAgentService travelAgentService;

//    private final TravelAgentQueryService travelAgentQueryService;

    public TravelAgentResource(TravelAgentService travelAgentService
//            , TravelAgentQueryService travelAgentQueryService
    ) {
        this.travelAgentService = travelAgentService;
//        this.travelAgentQueryService = travelAgentQueryService;
    }

    /**
     * {@code POST  /travel-agents} : Create a new travelAgent.
     *
     * @param travelAgent the travelAgent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new travelAgent, or with status {@code 400 (Bad Request)} if the travelAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/travel-agents")
    public ResponseEntity<TravelAgent> createTravelAgent(@RequestBody TravelAgent travelAgent) throws URISyntaxException {
        log.debug("REST request to save TravelAgent : {}", travelAgent);
        if (travelAgent.getId() != null) {
            throw new BadRequestAlertException("A new travelAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TravelAgent result = travelAgentService.save(travelAgent);
        return ResponseEntity.created(new URI("/api/travel-agents/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /travel-agents} : Updates an existing travelAgent.
     *
     * @param travelAgent the travelAgent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated travelAgent,
     * or with status {@code 400 (Bad Request)} if the travelAgent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the travelAgent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/travel-agents")
    public ResponseEntity<TravelAgent> updateTravelAgent(@RequestBody TravelAgent travelAgent) throws URISyntaxException {
        log.debug("REST request to update TravelAgent : {}", travelAgent);
        if (travelAgent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TravelAgent result = travelAgentService.save(travelAgent);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, travelAgent.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /travel-agents} : get all the travelAgents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of travelAgents in body.
     */
    @GetMapping("/travel-agents")
    public ResponseEntity<List<TravelAgent>> getAllTravelAgents(TravelAgentSearchRequestDTO travelAgentSearchRequestDTO, Pageable pageable) {
        log.debug("REST request to get a page of TravelAgents");
        Page<TravelAgent> page = travelAgentService.findAllBy(pageable, travelAgentSearchRequestDTO);
//        Page<TravelAgent> page = travelAgentQueryService.findByCriteria(travelAgentCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/travel-agents/list/template/download")
    @ResponseBody
    public ResponseEntity<Resource> serveFile() throws IOException {
        Resource file = loadAsResource();
        String filename = "TravelAgentTemplate.xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(file.contentLength())
                .body(file);
    }

    public Resource loadAsResource() {
        try {
            Resource resource = new ClassPathResource("downloads/TravelAgentTemplate.xlsx");
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
            }
        }
        catch (Exception e) {
        }
        return null;
    }


    /**
     * {@code GET  /travel-agents} : get all the travelAgents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of travelAgents in body.

    @GetMapping("/travel-agents/list")
    public ResponseEntity<List<TravelAgent>> getAllTravelAgentsList(Pageable pageable) {
        log.debug("REST request to get a page of TravelAgents");
//        Page<TravelAgent> page = travelAgentService.findAll(pageable);
        Page<TravelAgent> page = travelAgentQueryService.findByCriteriaList(travelAgentCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/

    /**
     * {@code GET  /travel-agents/:id} : get the "id" travelAgent.
     *
     * @param id the id of the travelAgent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the travelAgent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/travel-agents/{id}")
    public ResponseEntity<TravelAgent> getTravelAgent(@PathVariable Long id) {
        log.debug("REST request to get TravelAgent : {}", id);
        Optional<TravelAgent> travelAgent = travelAgentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(travelAgent);
    }

    @GetMapping("/travel-agents/api-credential/{username}/{password}")
    public ResponseEntity<List<TravelAgent>> getTravelAgentApiCredential(@PathVariable String username, @PathVariable String password) {
        log.debug("REST request to get TravelAgent : {}", username);
        if (username != null && password != null) {
            List<TravelAgent> travelAgent = travelAgentService.findAllByCgroupAndApiPassword(username, password);

            return ResponseEntity.ok(travelAgent);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * {@code DELETE  /travel-agents/:id} : delete the "id" travelAgent.
     *
     * @param id the id of the travelAgent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/travel-agents/{id}")
    public ResponseEntity<Void> deleteTravelAgent(@PathVariable Long id) {
        log.debug("REST request to delete TravelAgent : {}", id);
        travelAgentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }


    @ResponseStatus(OK)
    @Transactional
    @PostMapping("/travel-agents/list/import")
    public ResponseEntity<Void> importExcelFile(@RequestParam("file") MultipartFile files) throws IOException {
        HttpStatus status = HttpStatus.OK;

        Pageable page = PageRequest.of(0,99999);
        List<TravelAgent> travelAgents = travelAgentService.findAll(page).getContent();

        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        List<TravelAgent> updatedTravelAgent = new ArrayList<>();
        int index = 0;
        for (Row row: worksheet) {
            if (index > 0) {
                if(!checkIfRowIsEmpty(row)) {
                    System.out.println("Row "+index+" is not empty");
                    String custcode = row.getCell(0).getStringCellValue();
                    String email = row.getCell(1).getStringCellValue();
                    Optional<TravelAgent> travelAgentOptional = travelAgents.stream().filter(e -> e.getCustcode().contentEquals(custcode)).findFirst();
                    if(travelAgentOptional.isPresent()) {
//                        TravelAgent currentTa = travelAgentService.findOne(travelAgentOptional.get().getId()).get();
//                        System.out.println("Travel agent "+custcode+" exist");
                        travelAgentOptional.get().setProformaInvoiceRecipients(email);
                        updatedTravelAgent.add(travelAgentOptional.get());
                    }
                    else{
                        TravelAgent travelAgent = new TravelAgent();
                        travelAgent.setTravelAgentEmail(email);
                        travelAgent.setCustcode(custcode);
                        updatedTravelAgent.add(travelAgent);
                    }
                }
                else{
                    System.out.println("Empty row");
                }
            }
            index++;
        }

        if(updatedTravelAgent.size() > 0){
//            for(TravelAgent ta : updatedTravelAgent){
//                travelAgentService.save(ta);
//            }
            System.out.println("Update "+updatedTravelAgent.size()+" travel agent");
            travelAgentService.saveAll(updatedTravelAgent);
        }
        else{
            System.out.println("Travel agent  = 0");
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
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString()) && !cell.toString().contentEquals("")) {
                return false;
            }
        }
        return true;
    }
}
