package org.adastra.curriculum.service.mapper;

import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.domain.Language;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.dto.LanguageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Language} and its DTO {@link LanguageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {
    @Mapping(target = "biography", source = "biography", qualifiedByName = "biographyId")
    LanguageDTO toDto(Language s);

    @Named("biographyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BiographyDTO toDtoBiographyId(Biography biography);
}
