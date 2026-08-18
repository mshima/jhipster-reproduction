package com.okta.developer.notification.service.mapper;

import com.okta.developer.notification.domain.Authority;
import com.okta.developer.notification.domain.UserEntity;
import com.okta.developer.notification.service.dto.AdminUserRest;
import com.okta.developer.notification.service.dto.UserRest;
import java.util.*;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link UserEntity} and its DTO called {@link UserRest}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    public List<UserRest> usersToUserDTOs(List<UserEntity> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).toList();
    }

    public UserRest userToUserDTO(UserEntity user) {
        return new UserRest(user);
    }

    public List<AdminUserRest> usersToAdminUserDTOs(List<UserEntity> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).toList();
    }

    public AdminUserRest userToAdminUserDTO(UserEntity user) {
        return new AdminUserRest(user);
    }

    public List<UserEntity> userDTOsToUsers(List<AdminUserRest> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).toList();
    }

    public UserEntity userDTOToUser(AdminUserRest userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            UserEntity user = new UserEntity();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setCreatedBy(userDTO.getCreatedBy());
            user.setCreatedDate(userDTO.getCreatedDate());
            user.setLastModifiedBy(userDTO.getLastModifiedBy());
            user.setLastModifiedDate(userDTO.getLastModifiedDate());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString
                .stream()
                .map(string -> {
                    Authority auth = new Authority();
                    auth.setName(string);
                    return auth;
                })
                .collect(Collectors.toSet());
        }

        return authorities;
    }

    public UserEntity userFromId(String id) {
        if (id == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(id);
        return user;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public UserRest toDtoId(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserRest userDto = new UserRest();
        userDto.setId(user.getId());
        return userDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<UserRest> toDtoIdSet(Set<UserEntity> users) {
        if (users == null) {
            return Set.of();
        }

        Set<UserRest> userSet = new HashSet<>();
        for (UserEntity userEntity : users) {
            userSet.add(this.toDtoId(userEntity));
        }

        return userSet;
    }

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public UserRest toDtoLogin(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserRest userDto = new UserRest();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        return userDto;
    }

    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public Set<UserRest> toDtoLoginSet(Set<UserEntity> users) {
        if (users == null) {
            return Set.of();
        }

        Set<UserRest> userSet = new HashSet<>();
        for (UserEntity userEntity : users) {
            userSet.add(this.toDtoLogin(userEntity));
        }

        return userSet;
    }
}
