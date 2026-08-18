package com.okta.developer.notification.service.mapper;

import com.okta.developer.notification.domain.NotificationEntity;
import com.okta.developer.notification.domain.UserEntity;
import com.okta.developer.notification.service.dto.NotificationRest;
import com.okta.developer.notification.service.dto.UserRest;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationEntity} and its DTO {@link NotificationRest}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationRest, NotificationEntity> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    NotificationRest toDto(NotificationEntity s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserRest toDtoUserLogin(UserEntity userEntity);
}
