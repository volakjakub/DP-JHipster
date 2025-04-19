package org.adastra.curriculum.service;

import static org.adastra.curriculum.security.SecurityUtils.getCurrentUserLogin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.adastra.curriculum.domain.Project;
import org.adastra.curriculum.domain.Skill;
import org.adastra.curriculum.repository.ProjectRepository;
import org.adastra.curriculum.repository.SkillRepository;
import org.adastra.curriculum.service.dto.ProjectDTO;
import org.adastra.curriculum.service.dto.SkillDTO;
import org.adastra.curriculum.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.adastra.curriculum.domain.Project}.
 */
@Service
@Transactional
public class ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    private final SkillRepository skillRepository;

    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, SkillRepository skillRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectDTO save(ProjectDTO projectDTO) {
        LOG.debug("Request to save Project : {}", projectDTO);
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }
        if (!projectDTO.getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Cannot create Project for different Biography!");
        }

        for (SkillDTO skillDTO : projectDTO.getSkills()) {
            Optional<Skill> skill = skillRepository.findById(skillDTO.getId());
            if (skill.isPresent() && !skill.get().getBiography().getId().equals(projectDTO.getBiography().getId())) {
                throw new RuntimeException("Cannot assign Skill from different Biography!");
            }
        }

        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    /**
     * Update a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectDTO update(ProjectDTO projectDTO) {
        LOG.debug("Request to update Project : {}", projectDTO);
        if (canManipulate(projectDTO.getId())) {
            for (SkillDTO skillDTO : projectDTO.getSkills()) {
                Optional<Skill> skill = skillRepository.findById(skillDTO.getId());
                if (skill.isPresent() && !skill.get().getBiography().getId().equals(projectDTO.getBiography().getId())) {
                    throw new RuntimeException("Cannot assign Skill from different Biography!");
                }
            }

            Project project = projectMapper.toEntity(projectDTO);
            project = projectRepository.save(project);
            return projectMapper.toDto(project);
        } else {
            throw new RuntimeException("Project not assigned to biography owned by user!");
        }
    }

    /**
     * Partially update a project.
     *
     * @param projectDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectDTO> partialUpdate(ProjectDTO projectDTO) {
        LOG.debug("Request to partially update Project : {}", projectDTO);
        if (canManipulate(projectDTO.getId())) {
            for (SkillDTO skillDTO : projectDTO.getSkills()) {
                Optional<Skill> skill = skillRepository.findById(skillDTO.getId());
                if (skill.isPresent() && !skill.get().getBiography().getId().equals(projectDTO.getBiography().getId())) {
                    throw new RuntimeException("Cannot assign Skill from different Biography!");
                }
            }

            return projectRepository
                .findById(projectDTO.getId())
                .map(existingProject -> {
                    projectMapper.partialUpdate(existingProject, projectDTO);

                    return existingProject;
                })
                .map(projectRepository::save)
                .map(projectMapper::toDto);
        }
        return Optional.empty();
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Projects");
        return projectRepository.findAll(pageable).map(projectMapper::toDto);
    }

    /**
     * Get all the projects with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProjectDTO> findAllWithEagerRelationships(Pageable pageable) {
        return projectRepository.findAllWithEagerRelationships(pageable).map(projectMapper::toDto);
    }

    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        LOG.debug("Request to get Project : {}", id);
        return projectRepository.findOneWithEagerRelationships(id).map(projectMapper::toDto);
    }

    /**
     * Get all the projects by Biography ID.
     *
     * @param biographyId the biography ID.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAllByBiographyId(Long biographyId) {
        LOG.debug("Request to get all Projects by Biography ID : {}", biographyId);
        return projectRepository
            .findAllByBiographyIdOrderByStartAsc(biographyId)
            .stream()
            .map(projectMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Project : {}", id);
        if (canManipulate(id)) {
            projectRepository.deleteById(id);
        }
    }

    private boolean canManipulate(Long id) throws RuntimeException {
        Optional<String> login = getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new RuntimeException("User not logged in!");
        }

        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw new RuntimeException("Project not found!");
        }

        if (!project.get().getBiography().getUser().getLogin().equals(login.get())) {
            throw new RuntimeException("Project not assigned to biography owned by user!");
        }
        return true;
    }
}
