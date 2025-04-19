package org.adastra.curriculum.service.mapper;

import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.domain.Education;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.dto.EducationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Education} and its DTO {@link EducationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EducationMapper extends EntityMapper<EducationDTO, Education> {
    @Mapping(target = "biography", source = "biography", qualifiedByName = "biographyId")
    EducationDTO toDto(Education s);

    @Named("biographyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BiographyDTO toDtoBiographyId(Biography biography);
}
