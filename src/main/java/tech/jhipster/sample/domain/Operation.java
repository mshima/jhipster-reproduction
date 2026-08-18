package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Operation.
 */
@Table("operation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Operation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("date")
    private Instant date;

    @Column("description")
    private String description;

    @NotNull
    @Column("amount")
    private BigDecimal amount;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user", "operations" }, allowSetters = true)
    private BankAccount bankAccount;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "operations" }, allowSetters = true)
    private Set<Label> labels = new HashSet<>();

    @Column("bank_account_id")
    private Long bankAccountId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Operation date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public Operation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Operation amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.bankAccountId = bankAccount != null ? bankAccount.getId() : null;
    }

    public Operation bankAccount(BankAccount bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    public Set<Label> getLabels() {
        return this.labels;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    public Operation labels(Set<Label> labels) {
        this.setLabels(labels);
        return this;
    }

    public Operation addLabel(Label label) {
        this.labels.add(label);
        return this;
    }

    public Operation removeLabel(Label label) {
        this.labels.remove(label);
        return this;
    }

    public Long getBankAccountId() {
        return this.bankAccountId;
    }

    public void setBankAccountId(Long bankAccount) {
        this.bankAccountId = bankAccount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation)) {
            return false;
        }
        return getId() != null && getId().equals(((Operation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
