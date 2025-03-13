package org.adastra.curriculum.repository;

import java.util.List;
import org.adastra.curriculum.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByBiographyIdOrderByNameAsc(Long biographyId);
}
