package tech.nobb.task.engine.service;

import tech.nobb.task.engine.domain.checkrule.CompleteCheckRule;
import tech.nobb.task.engine.domain.checkrule.impl.PercentageCheckRule;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.domain.allocator.impl.ParallelAllocator;
import tech.nobb.task.engine.domain.allocator.impl.SerialAllocator;
import tech.nobb.task.engine.protocal.rest.dto.SimpleTaskCreateRequest;
import tech.nobb.task.engine.protocal.rest.dto.TaskCreateAndAssignRequest;
import org.springframework.stereotype.Service;
import tech.nobb.task.engine.domain.Task;

import java.util.Objects;

@Service
public class TaskCreateService extends BaseService{
    // 创建一个简单的任务，仅创建任务，不分配任务
    public Task newSimpleTask(SimpleTaskCreateRequest simpleTaskCreateRequest) {

        CompleteCheckRule checkRule = null;
        TaskAllocator allocator = null;
        if ("percentage".equals(simpleTaskCreateRequest.getCheckRule())) {
            checkRule = new PercentageCheckRule(Double.parseDouble(simpleTaskCreateRequest.getThreshold()), configRepository);
        }
        if ("parallel".equals(simpleTaskCreateRequest.getAllocator())) {
            allocator = new ParallelAllocator(configRepository);
        }
        if ("serial".equals(simpleTaskCreateRequest.getAllocator())) {
            allocator = new SerialAllocator(configRepository);
        }
        if (Objects.nonNull(checkRule) && Objects.nonNull(allocator)) {
            Task task = new Task(
                    simpleTaskCreateRequest.getName(),
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
    public void newTaskAndAssign(TaskCreateAndAssignRequest taskCreateAndAssignRequest) {
        Task task = newSimpleTask(new SimpleTaskCreateRequest(
                                    taskCreateAndAssignRequest.getName(),
                                    taskCreateAndAssignRequest.getThreshold(),
                                    taskCreateAndAssignRequest.getCheckRule(),
                                    taskCreateAndAssignRequest.getAllocator(),
                                    taskCreateAndAssignRequest.getOrder()));
        // 给执行者分配任务
        task.assign(taskCreateAndAssignRequest.getExecutors());
    }
}
