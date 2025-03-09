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
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "biography", source = "biography", qualifiedByName = "biographyId")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "skillNameSet")
    ProjectDTO toDto(Project s);

    @Mapping(target = "removeSkills", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    @Named("biographyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BiographyDTO toDtoBiographyId(Biography biography);

    @Named("skillName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SkillDTO toDtoSkillName(Skill skill);

    @Named("skillNameSet")
    default Set<SkillDTO> toDtoSkillNameSet(Set<Skill> skill) {
        return skill.stream().map(this::toDtoSkillName).collect(Collectors.toSet());
    }
}
