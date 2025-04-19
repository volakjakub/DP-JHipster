package org.adastra.curriculum.web.rest;

import static org.adastra.curriculum.domain.EducationAsserts.*;
import static org.adastra.curriculum.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.adastra.curriculum.IntegrationTest;
import org.adastra.curriculum.domain.Education;
import org.adastra.curriculum.domain.enumeration.EducationType;
import org.adastra.curriculum.repository.EducationRepository;
import org.adastra.curriculum.service.dto.EducationDTO;
import org.adastra.curriculum.service.mapper.EducationMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EducationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EducationResourceIT {

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    private static final EducationType DEFAULT_TYPE = EducationType.HIGH_SCHOOL;
    private static final EducationType UPDATED_TYPE = EducationType.BACHELOR;

    private static final LocalDate DEFAULT_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/educations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationMockMvc;

    private Education education;

    private Education insertedEducation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createEntity() {
        return new Education().school(DEFAULT_SCHOOL).type(DEFAULT_TYPE).start(DEFAULT_START).end(DEFAULT_END);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createUpdatedEntity() {
        return new Education().school(UPDATED_SCHOOL).type(UPDATED_TYPE).start(UPDATED_START).end(UPDATED_END);
    }

    @BeforeEach
    public void initTest() {
        education = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEducation != null) {
            educationRepository.delete(insertedEducation);
            insertedEducation = null;
        }
    }

    @Test
    @Transactional
    void createEducation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);
        var returnedEducationDTO = om.readValue(
            restEducationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(educationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EducationDTO.class
        );

        // Validate the Education in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEducation = educationMapper.toEntity(returnedEducationDTO);
        assertEducationUpdatableFieldsEquals(returnedEducation, getPersistedEducation(returnedEducation));

        insertedEducation = returnedEducation;
    }

    @Test
    @Transactional
    void createEducationWithExistingId() throws Exception {
        // Create the Education with an existing ID
        education.setId(1L);
        EducationDTO educationDTO = educationMapper.toDto(education);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSchoolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        education.setSchool(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        education.setType(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        education.setStart(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEducations() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.saveAndFlush(education);

        // Get all the educationList
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(education.getId().intValue())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }

    @Test
    @Transactional
    void getEducation() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.saveAndFlush(education);

        // Get the education
        restEducationMockMvc
            .perform(get(ENTITY_API_URL_ID, education.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(education.getId().intValue()))
            .andExpect(jsonPath("$.school").value(DEFAULT_SCHOOL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEducation() throws Exception {
        // Get the education
        restEducationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEducation() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.saveAndFlush(education);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the education
        Education updatedEducation = educationRepository.findById(education.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEducation are not directly saved in db
        em.detach(updatedEducation);
        updatedEducation.school(UPDATED_SCHOOL).type(UPDATED_TYPE).start(UPDATED_START).end(UPDATED_END);
        EducationDTO educationDTO = educationMapper.toDto(updatedEducation);

        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(educationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEducationToMatchAllProperties(updatedEducation);
    }

    @Test
    @Transactional
    void putNonExistingEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(educationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.saveAndFlush(education);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation.school(UPDATED_SCHOOL).start(UPDATED_START);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEducationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEducation, education),
            getPersistedEducation(education)
        );
    }

    @Test
    @Transactional
    void fullUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.saveAndFlush(education);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation.school(UPDATED_SCHOOL).type(UPDATED_TYPE).start(UPDATED_START).end(UPDATED_END);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEducationUpdatableFieldsEquals(partialUpdatedEducation, getPersistedEducation(partialUpdatedEducation));
    }

    @Test
    @Transactional
    void patchNonExistingEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(educationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducation() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.saveAndFlush(education);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the education
        restEducationMockMvc
            .perform(delete(ENTITY_API_URL_ID, education.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return educationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Education getPersistedEducation(Education education) {
        return educationRepository.findById(education.getId()).orElseThrow();
    }

    protected void assertPersistedEducationToMatchAllProperties(Education expectedEducation) {
        assertEducationAllPropertiesEquals(expectedEducation, getPersistedEducation(expectedEducation));
    }

    protected void assertPersistedEducationToMatchUpdatableProperties(Education expectedEducation) {
        assertEducationAllUpdatablePropertiesEquals(expectedEducation, getPersistedEducation(expectedEducation));
    }
}
