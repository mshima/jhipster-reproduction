package tech.jhipster.sample.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MapsIdUserProfileWithDTO.
 */
@Table("maps_id_user_profile_with_dto")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MapsIdUserProfileWithDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("date_of_birth")
    private Instant dateOfBirth;

    @org.springframework.data.annotation.Transient
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MapsIdUserProfileWithDTO id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public MapsIdUserProfileWithDTO dateOfBirth(Instant dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MapsIdUserProfileWithDTO user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapsIdUserProfileWithDTO)) {
            return false;
        }
        return getId() != null && getId().equals(((MapsIdUserProfileWithDTO) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapsIdUserProfileWithDTO{" +
            "id=" + getId() +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            "}";
    }
}
