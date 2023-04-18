package com.htsc.task.engine.domain;

import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;
import com.htsc.task.engine.domain.allocator.TaskAllocator;
import com.htsc.task.engine.repository.ConfigRepository;
import com.htsc.task.engine.repository.ExecutionRepository;
import com.htsc.task.engine.repository.TaskRepository;
import com.htsc.task.engine.repository.dataobj.TaskDO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Task {
    public enum Status {
        // 任务被创建
        CREATED,
        // 任务被认领
        PROCESSING,
        // 任务被取消
        CANCELED,
        // 任务完成
        COMPLETED
    }
    public enum Priority {
        HIGH,
        NORMAL,
    }
    private final String id;
    private String name;
    private Priority priority;
    private Map<String, Execution> executions;
    private Status status;
    private List<Task> subtask;
    private Task parent;
    private CompleteCheckRule completeCheckRule;
    private TaskAllocator allocator;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final TaskRepository taskRepository;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ExecutionRepository executionRepository;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ConfigRepository configRepository;

    // 构建一个全新的任务对象
    public Task(String name,
                Priority priority,
                Task parent,
                CompleteCheckRule completeCheckRule,
                TaskAllocator allocator,
                TaskRepository taskRepository,
                ExecutionRepository executionRepository,
                ConfigRepository configRepository
                ) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.priority = priority;
        this.subtask = new ArrayList<>();
        this.completeCheckRule = completeCheckRule;
        this.allocator = allocator;
        this.executions = new HashMap<String, Execution>();
        this.parent = parent;
        this.priority = Priority.NORMAL;
        this.status = Status.CREATED;
        this.taskRepository = taskRepository;
        this.executionRepository = executionRepository;
        this.configRepository = configRepository;
    }

    // 根据ID构建一个空任务对象
    public Task(String id,
                TaskRepository taskRepository,
                ExecutionRepository executionRepository,
                ConfigRepository configRepository) {
        this.id = id;
        this.taskRepository = taskRepository;
        this.executionRepository = executionRepository;
        this.configRepository = configRepository;
        this.subtask = new ArrayList<>();
        this.executions = new HashMap<String, Execution>();
    }

    // 完成一个任务
    public void complete(String executor) {
        Execution execution = executions.get(executor);
        execution.complete();
    }

    public void checkCompletion() {
        if (completeCheckRule.complete(this)) {
            // 将当前任务的状态设置成COMPLETED
            status = Status.COMPLETED;
            // 将TASK进行扫尾
            onCompleted();
        } else {
            allocator.allocate(this);
        }
    }

    private void onCompleted() {
        if (status.equals(Status.COMPLETED)) {
            // 将当前TASK的所有ACTIVATED，EXECUTING，SUSPENDED状态置成DROPPED
            executions.forEach((id, e) -> {
                if (e.getStatus().equals(Execution.Status.CREATED) ||
                        e.getStatus().equals(Execution.Status.EXECUTING) ||
                        e.getStatus().equals(Execution.Status.SUSPENDED)) {
                    e.drop();
                    e.save();
                }
            });
            save();
        }
    }

    // 一个执行者认领一个任务
    public void claim(String executor) {
        Execution execution = new Execution(this, executor, executionRepository);
        executions.put(executor, execution);
        execution.start();
        execution.save();
        if (status.equals(Status.CREATED)) {
            status = Status.PROCESSING;
        }
        save();
    }
    // 给一个执行者分配一个任务
    public void assign(String executor) {
        Execution execution = new Execution(this, executor, executionRepository);
        executions.put(executor, execution);
        allocator.allocate(this);
        if (status.equals(Status.CREATED)) {
            status = Status.PROCESSING;
        }
        save();
    }

    public void assign(List<String> executors) {
        executors.forEach(e -> {
            Execution execution = new Execution(this, e, executionRepository);
            execution.save();
            executions.put(e, execution);
        });
        allocator.allocate(this);
        if (status.equals(Status.CREATED)) {
            status = Status.PROCESSING;
        }
        save();
    }

    // 搁置一个任务
    public void suspend(String executor) {
        Execution execution = executions.get(executor);
        execution.suspend();
        execution.save();
    }

    // 取消搁置一个任务
    public void unsuspend(String executor) {
        Execution execution = executions.get(executor);
        execution.unsuspend();
        execution.save();
    }

    // 转让一个任务
    public void forward(String executor, String otherExecutor) {
        Execution execution = executions.get(executor);
        execution.forward(otherExecutor);
    }

    // 持久化当前任务对象
    public void save() {
        completeCheckRule.save();
        allocator.save();
        taskRepository.save(toDataObject());
    }

    // 根据当前对象id还原当前task对象
    public void restore() {

    }

    public TaskDO toDataObject() {
        if (Objects.nonNull(parent)) {
            return new TaskDO(id,
                    parent.getId(),
                    name, priority.name(),
                    status.name(),
                    completeCheckRule.getId(),
                    allocator.getId());
        } else {
            return new TaskDO(id,
                    "null",
                    name, priority.name(),
                    status.name(),
                    completeCheckRule.getId(),
                    allocator.getId());
        }

    }
    @Override
    public String toString() {
        return "{id : " + id + " name: " + name + " status: " + status +
                "\n executionManager: " + executions.toString() + "}";
    }
}
