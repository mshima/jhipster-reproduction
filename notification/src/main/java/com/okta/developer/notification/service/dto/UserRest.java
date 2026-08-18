package com.okta.developer.notification.service.dto;

import com.okta.developer.notification.domain.UserEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserRest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String login;

    public UserRest() {
        // Empty constructor needed for Jackson.
    }

    public UserRest(UserEntity user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRest userDTO = (UserRest) o;
        if (userDTO.getId() == null || getId() == null) {
            return false;
        }

        return Objects.equals(getId(), userDTO.getId()) && Objects.equals(getLogin(), userDTO.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRest{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            "}";
    }
}
