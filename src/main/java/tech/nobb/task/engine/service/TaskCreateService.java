package tech.nobb.task.engine.service;

import tech.nobb.task.engine.domain.checkrule.CompleteCheckRule;
import tech.nobb.task.engine.domain.checkrule.impl.PercentageCheckRule;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.domain.allocator.impl.ParallelAllocator;
import tech.nobb.task.engine.domain.allocator.impl.SerialAllocator;
import tech.nobb.task.engine.protocal.rest.request.CreateSimpleTaskRequest;
import tech.nobb.task.engine.protocal.rest.request.ClaimTaskRequest;
import tech.nobb.task.engine.protocal.rest.request.CreateAndAssignTaskRequest;
import org.springframework.stereotype.Service;
import tech.nobb.task.engine.domain.Task;

import java.util.Objects;

@Service
public class TaskCreateService extends BaseService{
    // 创建一个简单的任务，仅创建任务，不分配任务
    public Task newSimpleTask(CreateSimpleTaskRequest createSimpleTaskRequest) {

        CompleteCheckRule checkRule = null;
        TaskAllocator allocator = null;
        if ("percentage".equals(createSimpleTaskRequest.getCheckRule())) {
            checkRule = new PercentageCheckRule(Double.parseDouble(createSimpleTaskRequest.getThreshold()), configRepository);
        }
        if ("parallel".equals(createSimpleTaskRequest.getAllocator())) {
            allocator = new ParallelAllocator(configRepository);
        }
        if ("serial".equals(createSimpleTaskRequest.getAllocator())) {
            allocator = new SerialAllocator(configRepository);
        }
        if (Objects.nonNull(checkRule) && Objects.nonNull(allocator)) {
            Task task = new Task(
                    createSimpleTaskRequest.getName(),
                    Task.Priority.NORMAL,
                    null,
                    checkRule,
                    allocator,
                    taskRepository,
                    executionRepository,
                    configRepository
            );
            task.save();
            return task;
        }
        return null;
    }

    // 创建一个任务并分配执行人
    public void newTaskAndAssign(CreateAndAssignTaskRequest createAndAssignTaskRequest) {
        Task task = newSimpleTask(new CreateSimpleTaskRequest(
                                    createAndAssignTaskRequest.getName(),
                                    createAndAssignTaskRequest.getCheckRule(),
                                    createAndAssignTaskRequest.getThreshold(),
                                    createAndAssignTaskRequest.getAllocator(),
                                    createAndAssignTaskRequest.getOrder()));
        // 给执行者分配任务
        task.assign(createAndAssignTaskRequest.getExecutors());
    }

}
