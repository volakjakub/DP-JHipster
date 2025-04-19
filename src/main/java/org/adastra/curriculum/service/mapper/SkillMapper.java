package org.adastra.curriculum.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.domain.Project;
import org.adastra.curriculum.domain.Skill;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.dto.ProjectDTO;
import org.adastra.curriculum.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "biography", source = "biography", qualifiedByName = "biographyId")
    @Mapping(target = "projects", source = "projects", qualifiedByName = "projectIdSet")
    SkillDTO toDto(Skill s);

    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "removeProjects", ignore = true)
    Skill toEntity(SkillDTO skillDTO);

    @Named("biographyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BiographyDTO toDtoBiographyId(Biography biography);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);

    @Named("projectIdSet")
    default Set<ProjectDTO> toDtoProjectIdSet(Set<Project> project) {
        return project.stream().map(this::toDtoProjectId).collect(Collectors.toSet());
    }
}
