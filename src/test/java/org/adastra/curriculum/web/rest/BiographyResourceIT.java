package org.adastra.curriculum.web.rest;

import static org.adastra.curriculum.domain.BiographyAsserts.*;
import static org.adastra.curriculum.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.adastra.curriculum.IntegrationTest;
import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.repository.BiographyRepository;
import org.adastra.curriculum.repository.UserRepository;
import org.adastra.curriculum.service.BiographyService;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.mapper.BiographyMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BiographyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BiographyResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EMPLOYED_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMPLOYED_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/biographies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BiographyRepository biographyRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BiographyRepository biographyRepositoryMock;

    @Autowired
    private BiographyMapper biographyMapper;

    @Mock
    private BiographyService biographyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBiographyMockMvc;

    private Biography biography;

    private Biography insertedBiography;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Biography createEntity() {
        return new Biography()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .title(DEFAULT_TITLE)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .position(DEFAULT_POSITION)
            .employedFrom(DEFAULT_EMPLOYED_FROM)
            .image(DEFAULT_IMAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Biography createUpdatedEntity() {
        return new Biography()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .title(UPDATED_TITLE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .position(UPDATED_POSITION)
            .employedFrom(UPDATED_EMPLOYED_FROM)
            .image(UPDATED_IMAGE);
    }

    @BeforeEach
    public void initTest() {
        biography = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBiography != null) {
            biographyRepository.delete(insertedBiography);
            insertedBiography = null;
        }
    }

    @Test
    @Transactional
    void createBiography() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);
        var returnedBiographyDTO = om.readValue(
            restBiographyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BiographyDTO.class
        );

        // Validate the Biography in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBiography = biographyMapper.toEntity(returnedBiographyDTO);
        assertBiographyUpdatableFieldsEquals(returnedBiography, getPersistedBiography(returnedBiography));

        insertedBiography = returnedBiography;
    }

    @Test
    @Transactional
    void createBiographyWithExistingId() throws Exception {
        // Create the Biography with an existing ID
        biography.setId(1L);
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setFirstName(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setLastName(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setPhone(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setEmail(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStreetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setStreet(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setCity(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setCountry(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setPosition(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmployedFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        biography.setEmployedFrom(null);

        // Create the Biography, which fails.
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        restBiographyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBiographies() throws Exception {
        // Initialize the database
        insertedBiography = biographyRepository.saveAndFlush(biography);

        // Get all the biographyList
        restBiographyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(biography.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].employedFrom").value(hasItem(DEFAULT_EMPLOYED_FROM.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBiographiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(biographyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBiographyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(biographyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBiographiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(biographyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBiographyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(biographyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBiography() throws Exception {
        // Initialize the database
        insertedBiography = biographyRepository.saveAndFlush(biography);

        // Get the biography
        restBiographyMockMvc
            .perform(get(ENTITY_API_URL_ID, biography.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(biography.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.employedFrom").value(DEFAULT_EMPLOYED_FROM.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingBiography() throws Exception {
        // Get the biography
        restBiographyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBiography() throws Exception {
        // Initialize the database
        insertedBiography = biographyRepository.saveAndFlush(biography);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the biography
        Biography updatedBiography = biographyRepository.findById(biography.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBiography are not directly saved in db
        em.detach(updatedBiography);
        updatedBiography
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .title(UPDATED_TITLE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .position(UPDATED_POSITION)
            .employedFrom(UPDATED_EMPLOYED_FROM)
            .image(UPDATED_IMAGE);
        BiographyDTO biographyDTO = biographyMapper.toDto(updatedBiography);

        restBiographyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, biographyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(biographyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBiographyToMatchAllProperties(updatedBiography);
    }

    @Test
    @Transactional
    void putNonExistingBiography() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        biography.setId(longCount.incrementAndGet());

        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBiographyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, biographyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(biographyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBiography() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        biography.setId(longCount.incrementAndGet());

        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBiographyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(biographyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBiography() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        biography.setId(longCount.incrementAndGet());

        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBiographyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBiographyWithPatch() throws Exception {
        // Initialize the database
        insertedBiography = biographyRepository.saveAndFlush(biography);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the biography using partial update
        Biography partialUpdatedBiography = new Biography();
        partialUpdatedBiography.setId(biography.getId());

        partialUpdatedBiography
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .position(UPDATED_POSITION)
            .employedFrom(UPDATED_EMPLOYED_FROM)
            .image(UPDATED_IMAGE);

        restBiographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBiography.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBiography))
            )
            .andExpect(status().isOk());

        // Validate the Biography in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBiographyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBiography, biography),
            getPersistedBiography(biography)
        );
    }

    @Test
    @Transactional
    void fullUpdateBiographyWithPatch() throws Exception {
        // Initialize the database
        insertedBiography = biographyRepository.saveAndFlush(biography);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the biography using partial update
        Biography partialUpdatedBiography = new Biography();
        partialUpdatedBiography.setId(biography.getId());

        partialUpdatedBiography
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .title(UPDATED_TITLE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .position(UPDATED_POSITION)
            .employedFrom(UPDATED_EMPLOYED_FROM)
            .image(UPDATED_IMAGE);

        restBiographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBiography.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBiography))
            )
            .andExpect(status().isOk());

        // Validate the Biography in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBiographyUpdatableFieldsEquals(partialUpdatedBiography, getPersistedBiography(partialUpdatedBiography));
    }

    @Test
    @Transactional
    void patchNonExistingBiography() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        biography.setId(longCount.incrementAndGet());

        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBiographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, biographyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(biographyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBiography() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        biography.setId(longCount.incrementAndGet());

        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBiographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(biographyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBiography() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        biography.setId(longCount.incrementAndGet());

        // Create the Biography
        BiographyDTO biographyDTO = biographyMapper.toDto(biography);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBiographyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(biographyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Biography in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBiography() throws Exception {
        // Initialize the database
        insertedBiography = biographyRepository.saveAndFlush(biography);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the biography
        restBiographyMockMvc
            .perform(delete(ENTITY_API_URL_ID, biography.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return biographyRepository.count();
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

    protected Biography getPersistedBiography(Biography biography) {
        return biographyRepository.findById(biography.getId()).orElseThrow();
    }

    protected void assertPersistedBiographyToMatchAllProperties(Biography expectedBiography) {
        assertBiographyAllPropertiesEquals(expectedBiography, getPersistedBiography(expectedBiography));
    }

    protected void assertPersistedBiographyToMatchUpdatableProperties(Biography expectedBiography) {
        assertBiographyAllUpdatablePropertiesEquals(expectedBiography, getPersistedBiography(expectedBiography));
    }
}
