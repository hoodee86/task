package tech.nobb.task.engine.service;

import org.springframework.stereotype.Service;
import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.protocal.rest.request.ClaimTaskRequest;
import tech.nobb.task.engine.protocal.rest.request.CompleteTaskRequest;

@Service
public class TaskExecuteService extends BaseService {
    // 申领一个任务
    public void claimOneTask(ClaimTaskRequest claimTaskRequest) {
        Task task = new Task(claimTaskRequest.getTaskId(), taskRepository, executionRepository, configRepository);
        task.restore();
        task.claim(claimTaskRequest.getExecutor());
    }

    public void completeOneTask(CompleteTaskRequest completeTaskRequest) {
        Task task = new Task(completeTaskRequest.getTaskId(), taskRepository, executionRepository, configRepository);
        task.restore();
        task.complete(completeTaskRequest.getExecutor());
    }
}
