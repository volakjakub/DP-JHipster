package org.adastra.curriculum.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.adastra.curriculum.repository.SkillRepository;
import org.adastra.curriculum.security.AuthoritiesConstants;
import org.adastra.curriculum.service.SkillService;
import org.adastra.curriculum.service.dto.SkillDTO;
import org.adastra.curriculum.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.adastra.curriculum.domain.Skill}.
 */
@RestController
@RequestMapping("/api/skills")
public class SkillResource {

    private static final Logger LOG = LoggerFactory.getLogger(SkillResource.class);

    private static final String ENTITY_NAME = "skill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkillService skillService;

    private final SkillRepository skillRepository;

    public SkillResource(SkillService skillService, SkillRepository skillRepository) {
        this.skillService = skillService;
        this.skillRepository = skillRepository;
    }

    /**
     * {@code POST  /skills} : Create a new skill.
     *
     * @param skillDTO the skillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillDTO, or with status {@code 400 (Bad Request)} if the skill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody SkillDTO skillDTO) throws URISyntaxException {
        LOG.debug("REST request to save Skill : {}", skillDTO);
        if (skillDTO.getId() != null) {
            throw new BadRequestAlertException("A new skill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        skillDTO = skillService.save(skillDTO);
        return ResponseEntity.created(new URI("/api/skills/" + skillDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, skillDTO.getId().toString()))
            .body(skillDTO);
    }

    /**
     * {@code PUT  /skills/:id} : Updates an existing skill.
     *
     * @param id the id of the skillDTO to save.
     * @param skillDTO the skillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillDTO,
     * or with status {@code 400 (Bad Request)} if the skillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SkillDTO> updateSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SkillDTO skillDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Skill : {}, {}", id, skillDTO);
        if (skillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        skillDTO = skillService.update(skillDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillDTO.getId().toString()))
            .body(skillDTO);
    }

    /**
     * {@code PATCH  /skills/:id} : Partial updates given fields of an existing skill, field will ignore if it is null
     *
     * @param id the id of the skillDTO to save.
     * @param skillDTO the skillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillDTO,
     * or with status {@code 400 (Bad Request)} if the skillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the skillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SkillDTO> partialUpdateSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SkillDTO skillDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Skill partially : {}, {}", id, skillDTO);
        if (skillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillDTO> result = skillService.partialUpdate(skillDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /skills} : get all the skills.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skills in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<SkillDTO>> getAllSkills(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Skills");
        Page<SkillDTO> page = skillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /skills} : get all the skills by Biography ID.
     *
     * @param biographyId the biography ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skills in body.
     */
    @GetMapping("/biography")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<SkillDTO>> getAllProjectsByBiographyId(
        @RequestParam(name = "biographyId", required = true) Long biographyId
    ) {
        LOG.debug("REST request to get a Skills by Biography ID : {}", biographyId);
        List<SkillDTO> skillDTOS = skillService.findAllByBiographyId(biographyId);
        return ResponseEntity.ok().body(skillDTOS);
    }

    /**
     * {@code GET  /skills/:id} : get the "id" skill.
     *
     * @param id the id of the skillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + AuthoritiesConstants.ADMIN + "', '" + AuthoritiesConstants.USER + "')")
    public ResponseEntity<SkillDTO> getSkill(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Skill : {}", id);
        Optional<SkillDTO> skillDTO = skillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillDTO);
    }

    /**
     * {@code DELETE  /skills/:id} : delete the "id" skill.
     *
     * @param id the id of the skillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Skill : {}", id);
        skillService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
