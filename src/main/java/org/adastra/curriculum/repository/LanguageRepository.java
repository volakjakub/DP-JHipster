package org.adastra.curriculum.repository;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.adastra.curriculum.domain.Language;
import org.adastra.curriculum.domain.enumeration.LanguageName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByBiographyIdOrderByExpertiseDesc(Long biographyId);
    Optional<Language> findOneByNameAndBiographyId(@NotNull LanguageName name, Long biography_id);
}
