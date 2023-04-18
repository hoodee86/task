package tech.nobb.task.engine.repository;

import tech.nobb.task.engine.repository.dataobj.ExecutionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionDO, String> {
}
