package tech.nobb.task.engine.service;

import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.ExecutionRepository;
import tech.nobb.task.engine.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskAssignService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private ExecutionRepository executionRepository;
    @Autowired private ConfigRepository configRepository;

    public Execution assignTaskToOne(String taskId, String executor) {
        // 创建一个空的Task对象
        Task task = new Task(taskId,taskRepository,executionRepository,configRepository);
        task.restore();
        Execution execution = task.assign(executor);
        return execution;
    }
}
