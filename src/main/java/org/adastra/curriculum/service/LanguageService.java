package org.adastra.curriculum.service;

import static org.adastra.curriculum.security.SecurityUtils.getCurrentUserLogin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.adastra.curriculum.domain.Language;
import org.adastra.curriculum.repository.LanguageRepository;
import org.adastra.curriculum.service.dto.LanguageDTO;
import org.adastra.curriculum.service.mapper.LanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.adastra.curriculum.domain.Language}.
 */
@Service
@Transactional
public class LanguageService {

    private static final Logger LOG = LoggerFactory.getLogger(LanguageService.class);

    private final LanguageRepository languageRepository;

    private final LanguageMapper languageMapper;

    public LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    /**
     * Save a language.
     *
     * @param languageDTO the entity to save.
     * @return the persisted entity.
     */
    public LanguageDTO save(LanguageDTO languageDTO) {
        LOG.debug("Request to save Language : {}", languageDTO);
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }
        if (!languageDTO.getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Cannot create Language for different Biography!");
        }

        Optional<Language> languageOptional = languageRepository.findOneByNameAndBiographyId(
            languageDTO.getName(),
            languageDTO.getBiography().getId()
        );
        if (languageOptional.isPresent()) {
            throw new RuntimeException("Language already exists!");
        }

        Language language = languageMapper.toEntity(languageDTO);
        language = languageRepository.save(language);
        return languageMapper.toDto(language);
    }

    /**
     * Update a language.
     *
     * @param languageDTO the entity to save.
     * @return the persisted entity.
     */
    public LanguageDTO update(LanguageDTO languageDTO) {
        LOG.debug("Request to update Language : {}", languageDTO);
        if (canManipulate(languageDTO.getId())) {
            Optional<Language> languageOptional = languageRepository.findOneByNameAndBiographyId(
                languageDTO.getName(),
                languageDTO.getBiography().getId()
            );
            if (languageOptional.isPresent() && !languageOptional.get().getId().equals(languageDTO.getId())) {
                throw new RuntimeException("Language already exists!");
            }

            Language language = languageMapper.toEntity(languageDTO);
            language = languageRepository.save(language);
            return languageMapper.toDto(language);
        } else {
            throw new RuntimeException("Language not assigned to biography owned by user!");
        }
    }

    /**
     * Partially update a language.
     *
     * @param languageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LanguageDTO> partialUpdate(LanguageDTO languageDTO) {
        LOG.debug("Request to partially update Language : {}", languageDTO);
        if (canManipulate(languageDTO.getId())) {
            Optional<Language> languageOptional = languageRepository.findOneByNameAndBiographyId(
                languageDTO.getName(),
                languageDTO.getBiography().getId()
            );
            if (languageOptional.isPresent() && !languageOptional.get().getId().equals(languageDTO.getId())) {
                throw new RuntimeException("Language already exists!");
            }

            return languageRepository
                .findById(languageDTO.getId())
                .map(existingLanguage -> {
                    languageMapper.partialUpdate(existingLanguage, languageDTO);

                    return existingLanguage;
                })
                .map(languageRepository::save)
                .map(languageMapper::toDto);
        }
        return Optional.empty();
    }

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LanguageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Languages");
        return languageRepository.findAll(pageable).map(languageMapper::toDto);
    }

    /**
     * Get one language by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LanguageDTO> findOne(Long id) {
        LOG.debug("Request to get Language : {}", id);
        return languageRepository.findById(id).map(languageMapper::toDto);
    }

    /**
     * Get all the languages by Biography ID.
     *
     * @param biographyId the biography ID.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> findAllByBiographyId(Long biographyId) {
        LOG.debug("Request to get all Languages by Biography ID : {}", biographyId);
        return languageRepository
            .findAllByBiographyIdOrderByExpertiseDesc(biographyId)
            .stream()
            .map(languageMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Delete the language by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) throws RuntimeException {
        LOG.debug("Request to delete Language : {}", id);
        if (canManipulate(id)) {
            languageRepository.deleteById(id);
        }
    }

    private boolean canManipulate(Long id) throws RuntimeException {
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }

        Optional<Language> language = languageRepository.findById(id);
        if (language.isEmpty()) {
            throw new RuntimeException("Language not found!");
        }

        if (!language.get().getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Language not assigned to biography owned by user!");
        }
        return true;
    }
}
