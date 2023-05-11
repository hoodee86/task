package tech.nobb.task.engine.service;

import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignService extends BaseService {

    // 将任务分配给一个执行者
    public Execution assignTaskToOne(String taskId, String executor) {
        // 创建一个空的Task对象
        Task task = new Task(taskId,taskRepository, executionRepository, allocatorRepository, zeebeClient);
        task.restore();
        return task.assign(executor);
    }

    // 将任务分配给多个执行者
    public List<Execution> assignTask(String taskId, List<String> executors) {
        Task task = new Task(taskId, taskRepository, executionRepository, allocatorRepository, zeebeClient);
        task.restore();
        return task.assign(executors);
    }
}
