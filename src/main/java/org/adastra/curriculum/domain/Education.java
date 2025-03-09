package org.adastra.curriculum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.adastra.curriculum.domain.enumeration.EducationType;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "school", length = 50, nullable = false)
    private String school;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EducationType type;

    @NotNull
    @Column(name = "start", nullable = false)
    private LocalDate start;

    @Column(name = "jhi_end")
    private LocalDate end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "educations" }, allowSetters = true)
    private Biography biography;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Education id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchool() {
        return this.school;
    }

    public Education school(String school) {
        this.setSchool(school);
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public EducationType getType() {
        return this.type;
    }

    public Education type(EducationType type) {
        this.setType(type);
        return this;
    }

    public void setType(EducationType type) {
        this.type = type;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public Education start(LocalDate start) {
        this.setStart(start);
        return this;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public Education end(LocalDate end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Biography getBiography() {
        return this.biography;
    }

    public void setBiography(Biography biography) {
        this.biography = biography;
    }

    public Education biography(Biography biography) {
        this.setBiography(biography);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Education)) {
            return false;
        }
        return getId() != null && getId().equals(((Education) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Education{" +
            "id=" + getId() +
            ", school='" + getSchool() + "'" +
            ", type='" + getType() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
