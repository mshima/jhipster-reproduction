package com.okta.developer.notification.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.okta.developer.notification.domain.NotificationEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationRest implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private UserRest user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserRest getUser() {
        return user;
    }

    public void setUser(UserRest user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationRest)) {
            return false;
        }

        NotificationRest notificationRest = (NotificationRest) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationRest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationRest{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
