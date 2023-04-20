package tech.nobb.task.engine.repository;

import tech.nobb.task.engine.repository.dataobj.ExecutionPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionPO, String> {
    List<ExecutionPO> findByTaskId(String taskId);
}
