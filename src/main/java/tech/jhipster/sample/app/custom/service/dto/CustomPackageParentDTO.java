package tech.jhipster.sample.app.custom.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tech.jhipster.sample.app.custom.domain.CustomPackageParent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomPackageParentDTO implements Serializable {

    private Long id;

    private String parentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomPackageParentDTO)) {
            return false;
        }

        CustomPackageParentDTO customPackageParentDTO = (CustomPackageParentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customPackageParentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomPackageParentDTO{" +
            "id=" + getId() +
            ", parentName='" + getParentName() + "'" +
            "}";
    }
}
