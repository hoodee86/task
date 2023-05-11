package tech.nobb.task.engine.service;

import tech.nobb.task.engine.domain.ActionConfig;
import tech.nobb.task.engine.domain.allocator.Allocator;
import tech.nobb.task.engine.domain.allocator.ParallelAllocator;
import tech.nobb.task.engine.domain.allocator.ParallelWithPercentageAllocator;
import tech.nobb.task.engine.domain.allocator.SerialAllocator;
import tech.nobb.task.engine.protocal.rest.request.CreateTaskRequest;
import org.springframework.stereotype.Service;
import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.ActionConfigRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


@Service
public class TaskCreateService extends BaseService{
    // 创建一个简单的任务，仅创建任务，不分配任务
    public Task createTask(CreateTaskRequest createTaskRequest) {
        Allocator allocator = null;
        ActionConfig actionConfig = null;
        if ("PARALLEL-PERCENTAGE".equals(createTaskRequest.getAllocator())) {
            allocator = new ParallelWithPercentageAllocator(
                    (double)(createTaskRequest.getAllocatorConfig().get("threshold")),
                    allocatorRepository);
            // 设置任务行为控制
            actionConfig = new ActionConfig(actionConfigRepository);
            actionConfig.setClaim(false);
            actionConfig.setAssign(false);
            actionConfig.setAssignSubtask(false);
        }
        else if ("SERIAL".equals(createTaskRequest.getAllocator())) {
            allocator = new SerialAllocator(
                    (ArrayList<String>)createTaskRequest.getAllocatorConfig().get("order"),
                    allocatorRepository);
            // 将当前的executor改成order，也就是说，Serial的executor可以不设置
            createTaskRequest.setExecutors((ArrayList<String>)createTaskRequest.getAllocatorConfig().get("order"));
            actionConfig = new ActionConfig(actionConfigRepository);
            actionConfig.setClaim(false);
            actionConfig.setAssign(false);
            actionConfig.setAssignSubtask(false);
        } else { // 默认创建一个PARALLEL的任务
            allocator = new ParallelAllocator(allocatorRepository);
            actionConfig = new ActionConfig(actionConfigRepository);
        }
        if (Objects.nonNull(allocator)) {
            Task task = new Task(
                    createTaskRequest.getName(),
                    Task.Priority.NORMAL,
                    null,
                    allocator,
                    createTaskRequest.getZeebeJobKey(),
                    createTaskRequest.getOriginator(),
                    actionConfig,
                    taskRepository,
                    executionRepository,
                    allocatorRepository,
                    actionConfigRepository,
                    zeebeClient);

            // 如果executors没指定或者executors为空，则将执行人指定为自己
            // 也就是说，创建一个任务，至少有一个execution
            if (Objects.isNull(createTaskRequest.getExecutors()) ||
                    createTaskRequest.getExecutors().size() == 0) {
                task.assign(Arrays.asList(createTaskRequest.getOriginator()));
            } else {
                task.assign(createTaskRequest.getExecutors());
            }
            task.save();
            return task;
        }
        return null;
    }

}
