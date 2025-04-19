package org.adastra.curriculum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

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
    @Size(max = 50)
    @Column(name = "client", length = 50, nullable = false)
    private String client;

    @NotNull
    @Column(name = "start", nullable = false)
    private LocalDate start;

    @Column(name = "jhi_end")
    private LocalDate end;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "educations", "languages", "skills", "projects" }, allowSetters = true)
    private Biography biography;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_project__skills",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "skills_id")
    )
    @JsonIgnoreProperties(value = { "biography", "projects" }, allowSetters = true)
    private Set<Skill> skills = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Project name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return this.client;
    }

    public Project client(String client) {
        this.setClient(client);
        return this;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public Project start(LocalDate start) {
        this.setStart(start);
        return this;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public Project end(LocalDate end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getDescription() {
        return this.description;
    }

    public Project description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Biography getBiography() {
        return this.biography;
    }

    public void setBiography(Biography biography) {
        this.biography = biography;
    }

    public Project biography(Biography biography) {
        this.setBiography(biography);
        return this;
    }

    public Set<Skill> getSkills() {
        return this.skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Project skills(Set<Skill> skills) {
        this.setSkills(skills);
        return this;
    }

    public Project addSkills(Skill skill) {
        this.skills.add(skill);
        return this;
    }

    public Project removeSkills(Skill skill) {
        this.skills.remove(skill);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return getId() != null && getId().equals(((Project) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", client='" + getClient() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
