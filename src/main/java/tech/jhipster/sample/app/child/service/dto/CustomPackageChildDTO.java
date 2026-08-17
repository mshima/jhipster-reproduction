package tech.jhipster.sample.app.child.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.sample.app.custom.service.dto.CustomPackageParentDTO;
import tech.jhipster.sample.service.dto.UserDTO;

/**
 * A DTO for the {@link tech.jhipster.sample.app.child.domain.CustomPackageChild} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomPackageChildDTO implements Serializable {

    private Long id;

    private String childName;

    private UserDTO user;

    private CustomPackageParentDTO customPackageParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CustomPackageParentDTO getCustomPackageParent() {
        return customPackageParent;
    }

    public void setCustomPackageParent(CustomPackageParentDTO customPackageParent) {
        this.customPackageParent = customPackageParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomPackageChildDTO)) {
            return false;
        }

        CustomPackageChildDTO customPackageChildDTO = (CustomPackageChildDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customPackageChildDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomPackageChildDTO{" +
            "id=" + getId() +
            ", childName='" + getChildName() + "'" +
            ", user=" + getUser() +
            ", customPackageParent=" + getCustomPackageParent() +
            "}";
    }
}
