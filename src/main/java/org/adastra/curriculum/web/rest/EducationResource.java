package org.adastra.curriculum.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.adastra.curriculum.repository.EducationRepository;
import org.adastra.curriculum.service.EducationService;
import org.adastra.curriculum.service.dto.EducationDTO;
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
 * REST controller for managing {@link org.adastra.curriculum.domain.Education}.
 */
@RestController
@RequestMapping("/api/educations")
public class EducationResource {

    private static final Logger LOG = LoggerFactory.getLogger(EducationResource.class);

    private static final String ENTITY_NAME = "education";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationService educationService;

    private final EducationRepository educationRepository;

    public EducationResource(EducationService educationService, EducationRepository educationRepository) {
        this.educationService = educationService;
        this.educationRepository = educationRepository;
    }

    /**
     * {@code POST  /educations} : Create a new education.
     *
     * @param educationDTO the educationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationDTO, or with status {@code 400 (Bad Request)} if the education has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EducationDTO> createEducation(@Valid @RequestBody EducationDTO educationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Education : {}", educationDTO);
        if (educationDTO.getId() != null) {
            throw new BadRequestAlertException("A new education cannot already have an ID", ENTITY_NAME, "idexists");
        }
        educationDTO = educationService.save(educationDTO);
        return ResponseEntity.created(new URI("/api/educations/" + educationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, educationDTO.getId().toString()))
            .body(educationDTO);
    }

    /**
     * {@code PUT  /educations/:id} : Updates an existing education.
     *
     * @param id the id of the educationDTO to save.
     * @param educationDTO the educationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationDTO,
     * or with status {@code 400 (Bad Request)} if the educationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EducationDTO> updateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EducationDTO educationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Education : {}, {}", id, educationDTO);
        if (educationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        educationDTO = educationService.update(educationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, educationDTO.getId().toString()))
            .body(educationDTO);
    }

    /**
     * {@code PATCH  /educations/:id} : Partial updates given fields of an existing education, field will ignore if it is null
     *
     * @param id the id of the educationDTO to save.
     * @param educationDTO the educationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationDTO,
     * or with status {@code 400 (Bad Request)} if the educationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the educationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the educationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EducationDTO> partialUpdateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EducationDTO educationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Education partially : {}, {}", id, educationDTO);
        if (educationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EducationDTO> result = educationService.partialUpdate(educationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, educationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /educations} : get all the educations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EducationDTO>> getAllEducations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Educations");
        Page<EducationDTO> page = educationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /educations} : get all the educations by Biography ID.
     *
     * @param biographyId the biography ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educations in body.
     */
    @GetMapping("/biography")
    public ResponseEntity<List<EducationDTO>> getAllEducationsByBiographyId(
        @RequestParam(name = "biographyId", required = true) Long biographyId
    ) {
        LOG.debug("REST request to get a Educations by Biography ID : {}", biographyId);
        List<EducationDTO> educationDTOS = educationService.findAllByBiographyId(biographyId);
        return ResponseEntity.ok().body(educationDTOS);
    }

    /**
     * {@code GET  /educations/:id} : get the "id" education.
     *
     * @param id the id of the educationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EducationDTO> getEducation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Education : {}", id);
        Optional<EducationDTO> educationDTO = educationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(educationDTO);
    }

    /**
     * {@code DELETE  /educations/:id} : delete the "id" education.
     *
     * @param id the id of the educationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Education : {}", id);
        educationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
