package org.adastra.curriculum.service;

import static org.adastra.curriculum.security.SecurityUtils.getCurrentUserLogin;

import java.util.Optional;
import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.repository.BiographyRepository;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.mapper.BiographyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.adastra.curriculum.domain.Biography}.
 */
@Service
@Transactional
public class BiographyService {

    private static final Logger LOG = LoggerFactory.getLogger(BiographyService.class);

    private final BiographyRepository biographyRepository;

    private final BiographyMapper biographyMapper;

    public BiographyService(BiographyRepository biographyRepository, BiographyMapper biographyMapper) {
        this.biographyRepository = biographyRepository;
        this.biographyMapper = biographyMapper;
    }

    /**
     * Save a biography.
     *
     * @param biographyDTO the entity to save.
     * @return the persisted entity.
     */
    public BiographyDTO save(BiographyDTO biographyDTO) {
        LOG.debug("Request to save Biography : {}", biographyDTO);
        Optional<Biography> biographyOptional = biographyRepository.findOneByUsername(biographyDTO.getUser().getLogin());
        if (biographyOptional.isEmpty()) {
            Biography biography = biographyMapper.toEntity(biographyDTO);
            biography = biographyRepository.save(biography);
            return biographyMapper.toDto(biography);
        } else {
            throw new RuntimeException("Biography for user already exists!");
        }
    }

    /**
     * Update a biography.
     *
     * @param biographyDTO the entity to save.
     * @return the persisted entity.
     */
    public BiographyDTO update(BiographyDTO biographyDTO) throws RuntimeException {
        LOG.debug("Request to update Biography : {}", biographyDTO);
        if (canManipulate(biographyDTO.getId())) {
            Biography biography = biographyMapper.toEntity(biographyDTO);
            biography = biographyRepository.save(biography);
            return biographyMapper.toDto(biography);
        } else {
            throw new RuntimeException("Biography not owned by user!");
        }
    }

    /**
     * Partially update a biography.
     *
     * @param biographyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BiographyDTO> partialUpdate(BiographyDTO biographyDTO) throws RuntimeException {
        LOG.debug("Request to partially update Biography : {}", biographyDTO);
        if (canManipulate(biographyDTO.getId())) {
            return biographyRepository
                .findById(biographyDTO.getId())
                .map(existingBiography -> {
                    biographyMapper.partialUpdate(existingBiography, biographyDTO);

                    return existingBiography;
                })
                .map(biographyRepository::save)
                .map(biographyMapper::toDto);
        }
        return Optional.empty();
    }

    /**
     * Get all the biographies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BiographyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Biographies");
        return biographyRepository.findAll(pageable).map(biographyMapper::toDto);
    }

    /**
     * Get all the biographies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BiographyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return biographyRepository.findAllWithEagerRelationships(pageable).map(biographyMapper::toDto);
    }

    /**
     * Get one biography by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BiographyDTO> findOne(Long id) {
        LOG.debug("Request to get Biography : {}", id);
        return biographyRepository.findOneWithEagerRelationships(id).map(biographyMapper::toDto);
    }

    /**
     * Get one biography by username.
     *
     * @param username the username of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BiographyDTO> findOneByUsername(String username) {
        LOG.debug("Request to get Biography by username : {}", username);
        return biographyRepository.findOneByUsername(username).map(biographyMapper::toDto);
    }

    /**
     * Delete the biography by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) throws RuntimeException {
        LOG.debug("Request to delete Biography : {}", id);
        if (canManipulate(id)) {
            biographyRepository.deleteById(id);
        }
    }

    private boolean canManipulate(Long id) throws RuntimeException {
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }

        Optional<Biography> biography = biographyRepository.findById(id);
        if (biography.isEmpty()) {
            throw new RuntimeException("Biography not found!");
        }

        if (!biography.get().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Biography not owned by user!");
        }
        return true;
    }
}
