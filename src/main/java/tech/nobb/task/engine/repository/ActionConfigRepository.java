package tech.nobb.task.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.nobb.task.engine.repository.entity.ActionConfigEntity;

@Repository
public interface ActionConfigRepository extends JpaRepository<ActionConfigEntity, String> {
}
