package org.adastra.curriculum.web.rest;

import static org.adastra.curriculum.domain.LanguageAsserts.*;
import static org.adastra.curriculum.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.adastra.curriculum.IntegrationTest;
import org.adastra.curriculum.domain.Language;
import org.adastra.curriculum.domain.enumeration.LanguageName;
import org.adastra.curriculum.repository.LanguageRepository;
import org.adastra.curriculum.service.dto.LanguageDTO;
import org.adastra.curriculum.service.mapper.LanguageMapper;
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
 * Integration tests for the {@link LanguageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LanguageResourceIT {

    private static final LanguageName DEFAULT_NAME = LanguageName.CZECH;
    private static final LanguageName UPDATED_NAME = LanguageName.SLOVAK;

    private static final Integer DEFAULT_EXPERTISE = 1;
    private static final Integer UPDATED_EXPERTISE = 2;

    private static final String ENTITY_API_URL = "/api/languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguageMockMvc;

    private Language language;

    private Language insertedLanguage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createEntity() {
        return new Language().name(DEFAULT_NAME).expertise(DEFAULT_EXPERTISE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createUpdatedEntity() {
        return new Language().name(UPDATED_NAME).expertise(UPDATED_EXPERTISE);
    }

    @BeforeEach
    public void initTest() {
        language = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLanguage != null) {
            languageRepository.delete(insertedLanguage);
            insertedLanguage = null;
        }
    }

    @Test
    @Transactional
    void createLanguage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);
        var returnedLanguageDTO = om.readValue(
            restLanguageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LanguageDTO.class
        );

        // Validate the Language in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLanguage = languageMapper.toEntity(returnedLanguageDTO);
        assertLanguageUpdatableFieldsEquals(returnedLanguage, getPersistedLanguage(returnedLanguage));

        insertedLanguage = returnedLanguage;
    }

    @Test
    @Transactional
    void createLanguageWithExistingId() throws Exception {
        // Create the Language with an existing ID
        language.setId(1L);
        LanguageDTO languageDTO = languageMapper.toDto(language);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        language.setName(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpertiseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        language.setExpertise(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].expertise").value(hasItem(DEFAULT_EXPERTISE)));
    }

    @Test
    @Transactional
    void getLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.expertise").value(DEFAULT_EXPERTISE));
    }

    @Test
    @Transactional
    void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLanguage are not directly saved in db
        em.detach(updatedLanguage);
        updatedLanguage.name(UPDATED_NAME).expertise(UPDATED_EXPERTISE);
        LanguageDTO languageDTO = languageMapper.toDto(updatedLanguage);

        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLanguageToMatchAllProperties(updatedLanguage);
    }

    @Test
    @Transactional
    void putNonExistingLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.name(UPDATED_NAME).expertise(UPDATED_EXPERTISE);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLanguage, language), getPersistedLanguage(language));
    }

    @Test
    @Transactional
    void fullUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.name(UPDATED_NAME).expertise(UPDATED_EXPERTISE);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageUpdatableFieldsEquals(partialUpdatedLanguage, getPersistedLanguage(partialUpdatedLanguage));
    }

    @Test
    @Transactional
    void patchNonExistingLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the language
        restLanguageMockMvc
            .perform(delete(ENTITY_API_URL_ID, language.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return languageRepository.count();
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

    protected Language getPersistedLanguage(Language language) {
        return languageRepository.findById(language.getId()).orElseThrow();
    }

    protected void assertPersistedLanguageToMatchAllProperties(Language expectedLanguage) {
        assertLanguageAllPropertiesEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
    }

    protected void assertPersistedLanguageToMatchUpdatableProperties(Language expectedLanguage) {
        assertLanguageAllUpdatablePropertiesEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
    }
}
