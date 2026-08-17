package tech.jhipster.sample.app.child.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.app.child.domain.CustomPackageChild;
import tech.jhipster.sample.app.child.service.dto.CustomPackageChildDTO;
import tech.jhipster.sample.app.custom.domain.CustomPackageParent;
import tech.jhipster.sample.app.custom.service.dto.CustomPackageParentDTO;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.service.dto.UserDTO;

/**
 * Mapper for the entity {@link CustomPackageChild} and its DTO {@link CustomPackageChildDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomPackageChildMapper extends EntityMapper<CustomPackageChildDTO, CustomPackageChild> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "customPackageParent", source = "customPackageParent", qualifiedByName = "customPackageParentId")
    CustomPackageChildDTO toDto(CustomPackageChild s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("customPackageParentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomPackageParentDTO toDtoCustomPackageParentId(CustomPackageParent customPackageParent);
}
