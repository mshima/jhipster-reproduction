package com.mycompany.myapp.app.child.service.mapper;

import com.mycompany.myapp.app.child.domain.CustomPackageChild;
import com.mycompany.myapp.app.child.service.dto.CustomPackageChildDTO;
import com.mycompany.myapp.app.custom.domain.CustomPackageParent;
import com.mycompany.myapp.app.custom.service.dto.CustomPackageParentDTO;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

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
