package io.agroapp.application.web.rest;

import io.agroapp.application.domain.Presupuesto;
import io.agroapp.application.service.PresupuestoService;
import io.agroapp.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.agroapp.application.domain.Presupuesto}.
 */
@RestController
@RequestMapping("/api")
public class PresupuestoResource {

    private final Logger log = LoggerFactory.getLogger(PresupuestoResource.class);

    private static final String ENTITY_NAME = "presupuesto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresupuestoService presupuestoService;

    public PresupuestoResource(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    /**
     * {@code POST  /presupuestos} : Create a new presupuesto.
     *
     * @param presupuesto the presupuesto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presupuesto, or with status {@code 400 (Bad Request)} if the presupuesto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presupuestos")
    public ResponseEntity<Presupuesto> createPresupuesto(@Valid @RequestBody Presupuesto presupuesto) throws URISyntaxException {
        log.debug("REST request to save Presupuesto : {}", presupuesto);
        if (presupuesto.getId() != null) {
            throw new BadRequestAlertException("A new presupuesto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Presupuesto result = presupuestoService.save(presupuesto);
        return ResponseEntity.created(new URI("/api/presupuestos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presupuestos} : Updates an existing presupuesto.
     *
     * @param presupuesto the presupuesto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presupuesto,
     * or with status {@code 400 (Bad Request)} if the presupuesto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presupuesto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presupuestos")
    public ResponseEntity<Presupuesto> updatePresupuesto(@Valid @RequestBody Presupuesto presupuesto) throws URISyntaxException {
        log.debug("REST request to update Presupuesto : {}", presupuesto);
        if (presupuesto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Presupuesto result = presupuestoService.save(presupuesto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presupuesto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /presupuestos} : get all the presupuestos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presupuestos in body.
     */
    @GetMapping("/presupuestos")
    public ResponseEntity<List<Presupuesto>> getAllPresupuestos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Presupuestos");
        Page<Presupuesto> page = presupuestoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /presupuestos/:id} : get the "id" presupuesto.
     *
     * @param id the id of the presupuesto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presupuesto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presupuestos/{id}")
    public ResponseEntity<Presupuesto> getPresupuesto(@PathVariable Long id) {
        log.debug("REST request to get Presupuesto : {}", id);
        Optional<Presupuesto> presupuesto = presupuestoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presupuesto);
    }

    /**
     * {@code DELETE  /presupuestos/:id} : delete the "id" presupuesto.
     *
     * @param id the id of the presupuesto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presupuestos/{id}")
    public ResponseEntity<Void> deletePresupuesto(@PathVariable Long id) {
        log.debug("REST request to delete Presupuesto : {}", id);
        presupuestoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
