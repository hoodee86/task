package tech.nobb.task.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.nobb.task.engine.protocal.rest.vo.TaskVO;
import tech.nobb.task.engine.repository.entity.ExecutionEntity;

import java.util.List;

@Repository
public interface TaskViewRepository extends JpaRepository<ExecutionEntity, String> {
    @Query("select new tech.nobb.task.engine.protocal.rest.vo.TaskVO(e.taskId, e.id, t.name, t.priority, t.status, e.status, e.executorId, t.createTime, e.createTime, t.originatorId) from " +
            "TaskEntity t left join ExecutionEntity e on e.taskId=t.id where e.executorId = :executor")
    public List<TaskVO> findTaskVOSByExecutor(String executor);

    @Query("select new tech.nobb.task.engine.protocal.rest.vo.TaskVO(e.taskId, e.id, t.name, t.priority, t.status, e.status, e.executorId, t.createTime, e.createTime, t.originatorId) from " +
            "TaskEntity t left join ExecutionEntity e on e.taskId=t.id where e.taskId = :taskId")
    public List<TaskVO> findTaskVOsByTaskId(String taskId);
}