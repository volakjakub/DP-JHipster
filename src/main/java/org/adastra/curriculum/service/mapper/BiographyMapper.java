package org.adastra.curriculum.service.mapper;

import org.adastra.curriculum.domain.Biography;
import org.adastra.curriculum.domain.User;
import org.adastra.curriculum.service.dto.BiographyDTO;
import org.adastra.curriculum.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Biography} and its DTO {@link BiographyDTO}.
 */
@Mapper(componentModel = "spring")
public interface BiographyMapper extends EntityMapper<BiographyDTO, Biography> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    BiographyDTO toDto(Biography s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
