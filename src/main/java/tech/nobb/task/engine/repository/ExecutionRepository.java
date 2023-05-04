package tech.nobb.task.engine.repository;

import tech.nobb.task.engine.repository.entity.ExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionEntity, String> {
    List<ExecutionEntity> findByTaskId(String taskId);
}
