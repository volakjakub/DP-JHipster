package org.adastra.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.adastra.curriculum.domain.enumeration.LanguageName;

/**
 * A DTO for the {@link org.adastra.curriculum.domain.Language} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LanguageDTO implements Serializable {

    private Long id;

    @NotNull
    private LanguageName name;

    @NotNull
    private Integer expertise;

    private BiographyDTO biography;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageName getName() {
        return name;
    }

    public void setName(LanguageName name) {
        this.name = name;
    }

    public Integer getExpertise() {
        return expertise;
    }

    public void setExpertise(Integer expertise) {
        this.expertise = expertise;
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
        if (!(o instanceof LanguageDTO)) {
            return false;
        }

        LanguageDTO languageDTO = (LanguageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, languageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", expertise=" + getExpertise() +
            ", biography=" + getBiography() +
            "}";
    }
}
