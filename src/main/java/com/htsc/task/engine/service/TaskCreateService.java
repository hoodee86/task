package com.htsc.task.engine.service;

import com.htsc.task.engine.domain.*;
import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;
import com.htsc.task.engine.domain.checkrule.impl.PercentageCheckRule;
import com.htsc.task.engine.domain.allocator.TaskAllocator;
import com.htsc.task.engine.domain.allocator.impl.ParallelAllocator;
import com.htsc.task.engine.domain.allocator.impl.SerialAllocator;
import com.htsc.task.engine.protocal.rest.dto.SimpleTaskCreateRequest;
import com.htsc.task.engine.protocal.rest.dto.TaskCreateAndAssignRequest;
import com.htsc.task.engine.repository.ConfigRepository;
import com.htsc.task.engine.repository.ExecutionRepository;
import com.htsc.task.engine.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TaskCreateService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private ExecutionRepository executionRepository;
    @Autowired private ConfigRepository configRepository;

    // 创建一个简单的任务，仅创建任务，不分配任务
    public Task newSimpleTask(SimpleTaskCreateRequest simpleTaskCreateRequest) {

        CompleteCheckRule checkRule = null;
        TaskAllocator allocator = null;
        if ("percentage".equals(simpleTaskCreateRequest.getCheckRule())) {
            checkRule = new PercentageCheckRule(0.75, configRepository);
        }
        if ("parallel".equals(simpleTaskCreateRequest.getAllocator())) {
            allocator = new ParallelAllocator(configRepository);
        }
        if ("serial".equals(simpleTaskCreateRequest.getAllocator())) {
            allocator = new SerialAllocator(null, configRepository);
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
                                    taskCreateAndAssignRequest.getCheckRule(),
                                    taskCreateAndAssignRequest.getAllocator()));

    }
}
