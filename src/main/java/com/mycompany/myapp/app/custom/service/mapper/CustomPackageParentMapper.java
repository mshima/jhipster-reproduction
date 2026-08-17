package com.mycompany.myapp.app.custom.service.mapper;

import com.mycompany.myapp.app.custom.domain.CustomPackageParent;
import com.mycompany.myapp.app.custom.service.dto.CustomPackageParentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomPackageParent} and its DTO {@link CustomPackageParentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomPackageParentMapper extends EntityMapper<CustomPackageParentDTO, CustomPackageParent> {}
