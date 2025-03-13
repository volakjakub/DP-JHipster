package org.adastra.curriculum.service;

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
        Education education = educationMapper.toEntity(educationDTO);
        education = educationRepository.save(education);
        return educationMapper.toDto(education);
    }

    /**
     * Partially update a education.
     *
     * @param educationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EducationDTO> partialUpdate(EducationDTO educationDTO) {
        LOG.debug("Request to partially update Education : {}", educationDTO);

        return educationRepository
            .findById(educationDTO.getId())
            .map(existingEducation -> {
                educationMapper.partialUpdate(existingEducation, educationDTO);

                return existingEducation;
            })
            .map(educationRepository::save)
            .map(educationMapper::toDto);
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
        educationRepository.deleteById(id);
    }
}
