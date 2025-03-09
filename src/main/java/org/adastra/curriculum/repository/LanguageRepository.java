package org.adastra.curriculum.repository;

import java.util.List;
import org.adastra.curriculum.domain.Language;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByBiographyId(Long biographyId);
}
