package org.project.repositories;

import org.project.entities.Configs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigsRepository extends JpaRepository<Configs, String> {
}
