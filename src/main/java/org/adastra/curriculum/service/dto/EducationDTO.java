package org.adastra.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import org.adastra.curriculum.domain.enumeration.EducationType;

/**
 * A DTO for the {@link org.adastra.curriculum.domain.Education} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EducationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String school;

    @NotNull
    private EducationType type;

    @NotNull
    private LocalDate start;

    private LocalDate end;

    private BiographyDTO biography;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public EducationType getType() {
        return type;
    }

    public void setType(EducationType type) {
        this.type = type;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public BiographyDTO getBiography() {
        return biography;
    }

    public void setBiography(BiographyDTO biography) {
        this.biography = biography;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EducationDTO)) {
            return false;
        }

        EducationDTO educationDTO = (EducationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, educationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationDTO{" +
            "id=" + getId() +
            ", school='" + getSchool() + "'" +
            ", type='" + getType() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", biography=" + getBiography() +
            "}";
    }
}
