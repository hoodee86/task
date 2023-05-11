package tech.nobb.task.engine.service;

import org.springframework.stereotype.Service;
import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.protocal.rest.request.*;

@Service
public class TaskExecuteService extends BaseService {
    // 申领一个任务
    public void claimTask(ClaimTaskRequest claimTaskRequest) {
        Task task = new Task(
                claimTaskRequest.getTaskId(),
                taskRepository,
                executionRepository,
                allocatorRepository,
                zeebeClient);
        task.restore();
        task.claim(claimTaskRequest.getExecutor());
    }

    // 完成一个任务
    public void completeTask(CompleteTaskRequest completeTaskRequest) {
        Task task = new Task(
                completeTaskRequest.getTaskId(),
                taskRepository,
                executionRepository,
                allocatorRepository,
                zeebeClient);
        task.restore();
        task.complete(completeTaskRequest.getExecutor());
    }

    // 暂停一个任务
    public void suspendTask(SuspendTaskRequest suspendTaskRequest) {
        Task task = new Task(
                suspendTaskRequest.getTaskId(),
                taskRepository,
                executionRepository,
                allocatorRepository,
                zeebeClient);
        task.restore();
        task.suspend(suspendTaskRequest.getExecutor());
    }

    // 取消暂停一个任务
    public void unsuspendTask(UnsuspendTaskRequest unsuspendTaskRequest) {
        Task task = new Task(
                unsuspendTaskRequest.getTaskId(),
                taskRepository,
                executionRepository,
                allocatorRepository,
                zeebeClient);
        task.restore();
        task.unsuspend(unsuspendTaskRequest.getExecutor());
    }

    // 移交一个任务
    public void forwardTask(ForwardTaskRequest forwardTaskRequest) {
        Task task = new Task(
                forwardTaskRequest.getTaskId(),
                taskRepository,
                executionRepository,
                allocatorRepository,
                zeebeClient);
        task.restore();
        task.forward(forwardTaskRequest.getExecutor(), forwardTaskRequest.getOther());
    }
}
