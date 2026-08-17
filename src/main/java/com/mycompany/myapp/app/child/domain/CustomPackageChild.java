package com.mycompany.myapp.app.child.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.app.custom.domain.CustomPackageParent;
import com.mycompany.myapp.domain.User;
import java.io.Serial;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CustomPackageChild.
 */
@Table("custom_package_child")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomPackageChild implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("child_name")
    private String childName;

    @org.springframework.data.annotation.Transient
    private User user;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "customPackageChildren" }, allowSetters = true)
    private CustomPackageParent customPackageParent;

    @Column("user_id")
    private Long userId;

    @Column("custom_package_parent_id")
    private Long customPackageParentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomPackageChild id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChildName() {
        return this.childName;
    }

    public CustomPackageChild childName(String childName) {
        this.setChildName(childName);
        return this;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public CustomPackageChild user(User user) {
        this.setUser(user);
        return this;
    }

    public CustomPackageParent getCustomPackageParent() {
        return this.customPackageParent;
    }

    public void setCustomPackageParent(CustomPackageParent customPackageParent) {
        this.customPackageParent = customPackageParent;
        this.customPackageParentId = customPackageParent != null ? customPackageParent.getId() : null;
    }

    public CustomPackageChild customPackageParent(CustomPackageParent customPackageParent) {
        this.setCustomPackageParent(customPackageParent);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Long getCustomPackageParentId() {
        return this.customPackageParentId;
    }

    public void setCustomPackageParentId(Long customPackageParent) {
        this.customPackageParentId = customPackageParent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomPackageChild)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomPackageChild) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomPackageChild{" +
            "id=" + getId() +
            ", childName='" + getChildName() + "'" +
            "}";
    }
}
