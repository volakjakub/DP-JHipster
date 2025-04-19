package org.adastra.curriculum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.adastra.curriculum.domain.enumeration.LanguageName;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private LanguageName name;

    @NotNull
    @Column(name = "expertise", nullable = false)
    private Integer expertise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "educations", "languages", "skills", "projects" }, allowSetters = true)
    private Biography biography;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Language id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageName getName() {
        return this.name;
    }

    public Language name(LanguageName name) {
        this.setName(name);
        return this;
    }

    public void setName(LanguageName name) {
        this.name = name;
    }

    public Integer getExpertise() {
        return this.expertise;
    }

    public Language expertise(Integer expertise) {
        this.setExpertise(expertise);
        return this;
    }

    public void setExpertise(Integer expertise) {
        this.expertise = expertise;
    }

    public Biography getBiography() {
        return this.biography;
    }

    public void setBiography(Biography biography) {
        this.biography = biography;
    }

    public Language biography(Biography biography) {
        this.setBiography(biography);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        return getId() != null && getId().equals(((Language) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", expertise=" + getExpertise() +
            "}";
    }
}
