package tech.jhipster.sample.app.custom.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.app.custom.domain.CustomPackageParent;
import tech.jhipster.sample.app.custom.service.dto.CustomPackageParentDTO;

/**
 * Mapper for the entity {@link CustomPackageParent} and its DTO {@link CustomPackageParentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomPackageParentMapper extends EntityMapper<CustomPackageParentDTO, CustomPackageParent> {}
