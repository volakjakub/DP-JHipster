package org.adastra.curriculum.repository;

import java.util.List;
import org.adastra.curriculum.domain.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Education entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findAllByBiographyId(Long biographyId);
}
