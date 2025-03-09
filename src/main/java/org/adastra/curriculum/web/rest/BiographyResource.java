package org.adastra.curriculum.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.adastra.curriculum.repository.BiographyRepository;
import org.adastra.curriculum.service.BiographyService;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.adastra.curriculum.domain.Biography}.
 */
@RestController
@RequestMapping("/api/biographies")
public class BiographyResource {

    private static final Logger LOG = LoggerFactory.getLogger(BiographyResource.class);

    private static final String ENTITY_NAME = "biography";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BiographyService biographyService;

    private final BiographyRepository biographyRepository;

    public BiographyResource(BiographyService biographyService, BiographyRepository biographyRepository) {
        this.biographyService = biographyService;
        this.biographyRepository = biographyRepository;
    }

    /**
     * {@code POST  /biographies} : Create a new biography.
     *
     * @param biographyDTO the biographyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new biographyDTO, or with status {@code 400 (Bad Request)} if the biography has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BiographyDTO> createBiography(@Valid @RequestBody BiographyDTO biographyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Biography : {}", biographyDTO);
        if (biographyDTO.getId() != null) {
            throw new BadRequestAlertException("A new biography cannot already have an ID", ENTITY_NAME, "idexists");
        }
        biographyDTO = biographyService.save(biographyDTO);
        return ResponseEntity.created(new URI("/api/biographies/" + biographyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, biographyDTO.getId().toString()))
            .body(biographyDTO);
    }

    /**
     * {@code PUT  /biographies/:id} : Updates an existing biography.
     *
     * @param id the id of the biographyDTO to save.
     * @param biographyDTO the biographyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated biographyDTO,
     * or with status {@code 400 (Bad Request)} if the biographyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the biographyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BiographyDTO> updateBiography(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BiographyDTO biographyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Biography : {}, {}", id, biographyDTO);
        if (biographyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, biographyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!biographyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        biographyDTO = biographyService.update(biographyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, biographyDTO.getId().toString()))
            .body(biographyDTO);
    }

    /**
     * {@code PATCH  /biographies/:id} : Partial updates given fields of an existing biography, field will ignore if it is null
     *
     * @param id the id of the biographyDTO to save.
     * @param biographyDTO the biographyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated biographyDTO,
     * or with status {@code 400 (Bad Request)} if the biographyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the biographyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the biographyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BiographyDTO> partialUpdateBiography(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BiographyDTO biographyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Biography partially : {}, {}", id, biographyDTO);
        if (biographyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, biographyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!biographyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BiographyDTO> result = biographyService.partialUpdate(biographyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, biographyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /biographies} : get all the biographies.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of biographies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BiographyDTO>> getAllBiographies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Biographies");
        Page<BiographyDTO> page;
        if (eagerload) {
            page = biographyService.findAllWithEagerRelationships(pageable);
        } else {
            page = biographyService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /biographies/:id} : get the "id" biography.
     *
     * @param id the id of the biographyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the biographyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BiographyDTO> getBiography(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Biography : {}", id);
        Optional<BiographyDTO> biographyDTO = biographyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(biographyDTO);
    }

    /**
     * {@code DELETE  /biographies/:id} : delete the "id" biography.
     *
     * @param id the id of the biographyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBiography(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Biography : {}", id);
        biographyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
