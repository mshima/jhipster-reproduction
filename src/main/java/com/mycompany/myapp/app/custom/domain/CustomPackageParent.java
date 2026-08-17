package com.mycompany.myapp.app.custom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.app.child.domain.CustomPackageChild;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CustomPackageParent.
 */
@Table("custom_package_parent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomPackageParent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("parent_name")
    private String parentName;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user", "customPackageParent" }, allowSetters = true)
    private Set<CustomPackageChild> customPackageChildren = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomPackageParent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentName() {
        return this.parentName;
    }

    public CustomPackageParent parentName(String parentName) {
        this.setParentName(parentName);
        return this;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Set<CustomPackageChild> getCustomPackageChildren() {
        return this.customPackageChildren;
    }

    public void setCustomPackageChildren(Set<CustomPackageChild> customPackageChildren) {
        if (this.customPackageChildren != null) {
            this.customPackageChildren.forEach(i -> i.setCustomPackageParent(null));
        }
        if (customPackageChildren != null) {
            customPackageChildren.forEach(i -> i.setCustomPackageParent(this));
        }
        this.customPackageChildren = customPackageChildren;
    }

    public CustomPackageParent customPackageChildren(Set<CustomPackageChild> customPackageChildren) {
        this.setCustomPackageChildren(customPackageChildren);
        return this;
    }

    public CustomPackageParent addCustomPackageChild(CustomPackageChild customPackageChild) {
        this.customPackageChildren.add(customPackageChild);
        customPackageChild.setCustomPackageParent(this);
        return this;
    }

    public CustomPackageParent removeCustomPackageChild(CustomPackageChild customPackageChild) {
        this.customPackageChildren.remove(customPackageChild);
        customPackageChild.setCustomPackageParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomPackageParent)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomPackageParent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomPackageParent{" +
            "id=" + getId() +
            ", parentName='" + getParentName() + "'" +
            "}";
    }
}
