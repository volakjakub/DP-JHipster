package org.adastra.curriculum.service;

import static org.adastra.curriculum.security.SecurityUtils.getCurrentUserLogin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.adastra.curriculum.domain.Education;
import org.adastra.curriculum.repository.EducationRepository;
import org.adastra.curriculum.service.dto.EducationDTO;
import org.adastra.curriculum.service.mapper.EducationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.adastra.curriculum.domain.Education}.
 */
@Service
@Transactional
public class EducationService {

    private static final Logger LOG = LoggerFactory.getLogger(EducationService.class);

    private final EducationRepository educationRepository;

    private final EducationMapper educationMapper;

    public EducationService(EducationRepository educationRepository, EducationMapper educationMapper) {
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    /**
     * Save a education.
     *
     * @param educationDTO the entity to save.
     * @return the persisted entity.
     */
    public EducationDTO save(EducationDTO educationDTO) {
        LOG.debug("Request to save Education : {}", educationDTO);
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }
        if (!educationDTO.getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Cannot create Education for different Biography!");
        }

        Education education = educationMapper.toEntity(educationDTO);
        education = educationRepository.save(education);
        return educationMapper.toDto(education);
    }

    /**
     * Update a education.
     *
     * @param educationDTO the entity to save.
     * @return the persisted entity.
     */
    public EducationDTO update(EducationDTO educationDTO) {
        LOG.debug("Request to update Education : {}", educationDTO);
        if (canManipulate(educationDTO.getId())) {
            Education education = educationMapper.toEntity(educationDTO);
            education = educationRepository.save(education);
            return educationMapper.toDto(education);
        } else {
            throw new RuntimeException("Education not assigned to biography owned by user!");
        }
    }

    /**
     * Partially update a education.
     *
     * @param educationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EducationDTO> partialUpdate(EducationDTO educationDTO) {
        LOG.debug("Request to partially update Education : {}", educationDTO);
        if (canManipulate(educationDTO.getId())) {
            return educationRepository
                .findById(educationDTO.getId())
                .map(existingEducation -> {
                    educationMapper.partialUpdate(existingEducation, educationDTO);

                    return existingEducation;
                })
                .map(educationRepository::save)
                .map(educationMapper::toDto);
        }
        return Optional.empty();
    }

    /**
     * Get all the educations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EducationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Educations");
        return educationRepository.findAll(pageable).map(educationMapper::toDto);
    }

    /**
     * Get all the educations by Biography ID.
     *
     * @param biographyId the biography ID.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EducationDTO> findAllByBiographyId(Long biographyId) {
        LOG.debug("Request to get all Educations by Biography ID : {}", biographyId);
        return educationRepository
            .findAllByBiographyIdOrderByStartAsc(biographyId)
            .stream()
            .map(educationMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get one education by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EducationDTO> findOne(Long id) {
        LOG.debug("Request to get Education : {}", id);
        return educationRepository.findById(id).map(educationMapper::toDto);
    }

    /**
     * Delete the education by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Education : {}", id);
        if (canManipulate(id)) {
            educationRepository.deleteById(id);
        }
    }

    private boolean canManipulate(Long id) throws RuntimeException {
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }

        Optional<Education> education = educationRepository.findById(id);
        if (education.isEmpty()) {
            throw new RuntimeException("Education not found!");
        }

        if (!education.get().getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Education not assigned to biography owned by user!");
        }
        return true;
    }
}
