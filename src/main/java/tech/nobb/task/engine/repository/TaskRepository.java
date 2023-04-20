package tech.nobb.task.engine.repository;

import tech.nobb.task.engine.repository.dataobj.TaskPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskPO, String> {
    List<TaskPO> findByParent(String id);
}
