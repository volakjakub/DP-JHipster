package org.adastra.curriculum.service.mapper;

import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.domain.Skill;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "biography", source = "biography", qualifiedByName = "biographyId")
    SkillDTO toDto(Skill s);

    @Named("biographyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BiographyDTO toDtoBiographyId(Biography biography);
}
