package org.adastra.curriculum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "expertise", nullable = false)
    private Integer expertise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "educations", "languages", "skills" }, allowSetters = true)
    private Biography biography;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Skill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Skill name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExpertise() {
        return this.expertise;
    }

    public Skill expertise(Integer expertise) {
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

    public Skill biography(Biography biography) {
        this.setBiography(biography);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        return getId() != null && getId().equals(((Skill) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", expertise=" + getExpertise() +
            "}";
    }
}
