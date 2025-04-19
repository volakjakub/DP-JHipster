package org.adastra.curriculum.repository;

import java.util.List;
import java.util.Optional;
import org.adastra.curriculum.domain.Project;
import org.springframework.data.domain.Page;

public interface ProjectRepositoryWithBagRelationships {
    Optional<Project> fetchBagRelationships(Optional<Project> project);

    List<Project> fetchBagRelationships(List<Project> projects);

    Page<Project> fetchBagRelationships(Page<Project> projects);
}
