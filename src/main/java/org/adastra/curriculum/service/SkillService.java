package org.adastra.curriculum.service;

import static org.adastra.curriculum.security.SecurityUtils.getCurrentUserLogin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.adastra.curriculum.domain.Skill;
import org.adastra.curriculum.repository.SkillRepository;
import org.adastra.curriculum.service.dto.SkillDTO;
import org.adastra.curriculum.service.mapper.SkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.adastra.curriculum.domain.Skill}.
 */
@Service
@Transactional
public class SkillService {

    private static final Logger LOG = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    public SkillService(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    /**
     * Save a skill.
     *
     * @param skillDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillDTO save(SkillDTO skillDTO) {
        LOG.debug("Request to save Skill : {}", skillDTO);
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }
        if (!skillDTO.getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Cannot create Skill for different Biography!");
        }

        Optional<Skill> skillOptional = skillRepository.findOneByNameAndBiographyId(skillDTO.getName(), skillDTO.getBiography().getId());
        if (skillOptional.isPresent()) {
            throw new RuntimeException("Skill already exists!");
        }

        Skill skill = skillMapper.toEntity(skillDTO);
        skill = skillRepository.save(skill);
        return skillMapper.toDto(skill);
    }

    /**
     * Update a skill.
     *
     * @param skillDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillDTO update(SkillDTO skillDTO) {
        LOG.debug("Request to update Skill : {}", skillDTO);
        if (canManipulate(skillDTO.getId())) {
            Optional<Skill> skillOptional = skillRepository.findOneByNameAndBiographyId(
                skillDTO.getName(),
                skillDTO.getBiography().getId()
            );
            if (skillOptional.isPresent()) {
                throw new RuntimeException("Skill already exists!");
            }

            Skill skill = skillMapper.toEntity(skillDTO);
            skill = skillRepository.save(skill);
            return skillMapper.toDto(skill);
        } else {
            throw new RuntimeException("Skill not assigned to biography owned by user!");
        }
    }

    /**
     * Partially update a skill.
     *
     * @param skillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SkillDTO> partialUpdate(SkillDTO skillDTO) {
        LOG.debug("Request to partially update Skill : {}", skillDTO);
        if (canManipulate(skillDTO.getId())) {
            return skillRepository
                .findById(skillDTO.getId())
                .map(existingSkill -> {
                    skillMapper.partialUpdate(existingSkill, skillDTO);

                    return existingSkill;
                })
                .map(skillRepository::save)
                .map(skillMapper::toDto);
        }
        return Optional.empty();
    }

    /**
     * Get all the skills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SkillDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Skills");
        return skillRepository.findAll(pageable).map(skillMapper::toDto);
    }

    /**
     * Get all the skills by Biography ID.
     *
     * @param biographyId the biography ID.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SkillDTO> findAllByBiographyId(Long biographyId) {
        LOG.debug("Request to get all Skills by Biography ID : {}", biographyId);
        return skillRepository
            .findAllByBiographyIdOrderByNameAsc(biographyId)
            .stream()
            .map(skillMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get one skill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SkillDTO> findOne(Long id) {
        LOG.debug("Request to get Skill : {}", id);
        return skillRepository.findById(id).map(skillMapper::toDto);
    }

    /**
     * Delete the skill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Skill : {}", id);
        if (canManipulate(id)) {
            skillRepository.deleteById(id);
        }
    }

    private boolean canManipulate(Long id) throws RuntimeException {
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }

        Optional<Skill> skill = skillRepository.findById(id);
        if (skill.isEmpty()) {
            throw new RuntimeException("Skill not found!");
        }

        if (!skill.get().getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Skill not assigned to biography owned by user!");
        }
        return true;
    }
}
