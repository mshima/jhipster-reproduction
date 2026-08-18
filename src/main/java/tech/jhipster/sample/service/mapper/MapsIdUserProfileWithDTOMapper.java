package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.MapsIdUserProfileWithDTO;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.service.dto.MapsIdUserProfileWithDTODTO;
import tech.jhipster.sample.service.dto.UserDTO;

/**
 * Mapper for the entity {@link MapsIdUserProfileWithDTO} and its DTO {@link MapsIdUserProfileWithDTODTO}.
 */
@Mapper(componentModel = "spring")
public interface MapsIdUserProfileWithDTOMapper extends EntityMapper<MapsIdUserProfileWithDTODTO, MapsIdUserProfileWithDTO> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    MapsIdUserProfileWithDTODTO toDto(MapsIdUserProfileWithDTO s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
