package org.adastra.curriculum.repository;

import java.util.List;
import java.util.Optional;
import org.adastra.curriculum.domain.Biography;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Biography entity.
 */
@Repository
public interface BiographyRepository extends JpaRepository<Biography, Long> {
    default Optional<Biography> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Biography> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Biography> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select biography from Biography biography left join fetch biography.user",
        countQuery = "select count(biography) from Biography biography"
    )
    Page<Biography> findAllWithToOneRelationships(Pageable pageable);

    @Query("select biography from Biography biography left join fetch biography.user")
    List<Biography> findAllWithToOneRelationships();

    @Query("select biography from Biography biography left join fetch biography.user where biography.id =:id")
    Optional<Biography> findOneWithToOneRelationships(@Param("id") Long id);
}
