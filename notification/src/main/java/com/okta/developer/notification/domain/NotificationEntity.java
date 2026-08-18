package com.okta.developer.notification.domain;

import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A NotificationEntity.
 */
@Table("notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("title")
    private String title;

    @org.springframework.data.annotation.Transient
    private UserEntity user;

    @Column("user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public NotificationEntity title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public NotificationEntity user(UserEntity user) {
        this.setUser(user);
        return this;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
