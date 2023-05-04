package tech.nobb.task.engine.domain;

import io.camunda.zeebe.client.ZeebeClient;
import tech.nobb.task.engine.domain.allocator.impl.ParallelAllocator;
import tech.nobb.task.engine.domain.allocator.impl.SerialAllocator;
import tech.nobb.task.engine.domain.checkrule.CompleteCheckRule;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.domain.checkrule.impl.PercentageCheckRule;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.ExecutionRepository;
import tech.nobb.task.engine.repository.TaskRepository;
import tech.nobb.task.engine.repository.entity.ConfigEntity;
import tech.nobb.task.engine.repository.entity.ExecutionEntity;
import tech.nobb.task.engine.repository.entity.TaskEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

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
    // 增加创建”信息“和创建时间
    private String originator;
    private Date createTime;
    private Map<String, Execution> executions;
    private Status status;
    private Map<String, Task> subtask;
    private String root;
    private String parent;
    private CompleteCheckRule completeCheckRule;
    private TaskAllocator allocator;
    private long zeebeJobKey;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final TaskRepository taskRepository;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ExecutionRepository executionRepository;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ConfigRepository configRepository;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private ZeebeClient zeebeClient;

    // 构建一个全新的任务对象, 不是流程触发的，是人工触发的。
    public Task(String name,
                Priority priority,
                String parent,
                CompleteCheckRule completeCheckRule,
                TaskAllocator allocator,
                long zeebeJobKey,
                String originator,
                TaskRepository taskRepository,
                ExecutionRepository executionRepository,
                ConfigRepository configRepository,
                ZeebeClient zeebeClient) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.priority = priority;
        this.subtask = new HashMap<String, Task>();
        this.completeCheckRule = completeCheckRule;
        this.allocator = allocator;
        this.zeebeJobKey = zeebeJobKey;
        this.executions = new HashMap<String, Execution>();
        this.root = "-1";
        this.parent = parent;
        this.priority = Priority.NORMAL;
        this.status = Status.CREATED;
        this.taskRepository = taskRepository;
        this.executionRepository = executionRepository;
        this.configRepository = configRepository;
        this.zeebeClient = zeebeClient;
        this.originator = originator;
        this.createTime = new Date();
    }

    // 根据ID构建一个空任务对象
    public Task(String id,
                TaskRepository taskRepository,
                ExecutionRepository executionRepository,
                ConfigRepository configRepository,
                ZeebeClient zeebeClient) {
        this.id = id;
        this.taskRepository = taskRepository;
        this.executionRepository = executionRepository;
        this.configRepository = configRepository;
        this.subtask = new HashMap<>();
        this.executions = new HashMap<String, Execution>();
        this.zeebeClient = zeebeClient;
    }

    // 完成一个任务
    public void complete(String executor) {
        Execution execution = executions.get(executor);
        execution.complete();
        execution.save();
    }

    public void checkCompletion() {
        if (completeCheckRule.complete(this)) {
            // 将当前任务的状态设置成COMPLETED
            status = Status.COMPLETED;
            // 将TASK进行扫尾
            onCompleted();
            // 向zeebe broker发送该任务完成
            if (zeebeJobKey !=  -1) {
                zeebeClient.newCompleteCommand(zeebeJobKey).send().join();
            }
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
        Execution execution = new Execution(this, executor, Execution.Status.CREATED, executionRepository);
        executions.put(executor, execution);
        execution.start();
        execution.save();
        if (status.equals(Status.CREATED)) {
            status = Status.PROCESSING;
        }
        save();
    }
    // 给一个执行者分配一个任务
    public Execution assign(String executor) {
        Execution execution = new Execution(this, executor, Execution.Status.CREATED, executionRepository);
        executions.put(executor, execution);
        allocator.allocate(this);
        if (status.equals(Status.CREATED)) {
            status = Status.PROCESSING;
        }
        save();
        return execution;
    }

    public List<Execution> assign(List<String> executors) {
        List<Execution> results = new ArrayList<>();
        executors.forEach(e -> {
            Execution execution = new Execution(this, e, Execution.Status.CREATED, executionRepository);
            execution.save();
            executions.put(e, execution);
            results.add(execution);
        });
        allocator.allocate(this);
        if (status.equals(Status.CREATED)) {
            status = Status.PROCESSING;
        }
        save();
        return results;
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
        // 自己不可以转发给自己
        if (executor.equals(otherExecutor)) {
            return;
        }
        Execution execution = executions.get(executor);
        Execution forwardExecution = execution.forward(otherExecutor);
        executions.put(otherExecutor, forwardExecution);
        execution.save();
        forwardExecution.save();
    }

    // 持久化当前任务对象
    public void save() {
        completeCheckRule.save();
        allocator.save();
        taskRepository.save(toEntity());
    }

    // 根据当前对象id还原当前task对象
    public Task restore() {
        return restore(id);
    }

    private Task restore(String id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseGet(null);
        if (Objects.isNull(taskEntity)) {
            return null;
        } else {
            // 还原当前的task对象
            name = taskEntity.getName();
            root = taskEntity.getRoot();
            status = Status.valueOf(taskEntity.getStatus());
            priority = Priority.valueOf(taskEntity.getPriority());
            zeebeJobKey = taskEntity.getZeebeJobKey();
            if ("null".equals(taskEntity.getParent())) {
                parent = "null";
            } else {
                parent = taskEntity.getParent();
            }
            // 还原配置信息
            ConfigEntity allocatorPO = configRepository.findById(taskEntity.getAllocatorId()).orElseGet(null);
            ConfigEntity completeCheckRulePO = configRepository.findById(taskEntity.getCheckRuleId()).orElseGet(null);
            if (Objects.isNull(allocatorPO) || Objects.isNull(completeCheckRulePO)) {
                return null;
            }
            if (allocatorPO.getType().equals("ALLOCATOR")) {
                if (allocatorPO.getName().equals("PARALLEL")) {
                    allocator = new ParallelAllocator(taskEntity.getAllocatorId(), configRepository);
                    allocator.restore();
                } else if (allocatorPO.getName().equals("SERIAL")) {
                    allocator = new SerialAllocator(taskEntity.getAllocatorId(), configRepository);
                    allocator.restore();
                }
            }
            if (completeCheckRulePO.getType().equals("COMPLETE_CHECK_RULE")) {
                if (completeCheckRulePO.getName().equals("PERCENT_CHECK_RULE")) {
                    completeCheckRule = new PercentageCheckRule(taskEntity.getCheckRuleId(), configRepository);
                    completeCheckRule.restore();
                }
            }

            // 还原executions
            List<ExecutionEntity> executionEntities = executionRepository.findByTaskId(id);
            executionEntities.forEach(
                    executionEntity -> {
                        // FORWARDED状态需要被丢弃掉，没有意义
                        if (!executionEntity.getStatus().equals("FORWARDED")) {
                            executions.put(executionEntity.getExecutorId(), new Execution(
                                    executionEntity.getId(),
                                    executionEntity.getExecutorId(),
                                    executionEntity.getForwardId(),
                                    Execution.Status.valueOf(executionEntity.getStatus()),
                                    this,
                                    executionEntity.getCreateTime(),
                                    executionRepository));
                        }
                    }
            );

            // 初始化构建subtasks
            List<TaskEntity> subtaskEntities = taskRepository.findByParent(taskEntity.getId());
            // 递归restore每一个子任务
            subtaskEntities.forEach(
                    subtaskEntity -> {
                        subtask.put(subtaskEntity.getId(),
                                new Task(subtaskEntity.getId(),
                                        taskRepository,
                                        executionRepository,
                                        configRepository,
                                        zeebeClient).
                                        restore());
                    }
            );
            return this;
        }
    }

    public Task root() {
        return restore(root);
    }

    public Task parent() {
        return restore(parent);
    }

    public TaskEntity toEntity() {
        if (Objects.nonNull(parent)) {
            return new TaskEntity(id,
                    parent,
                    this.id,
                    name, priority.name(),
                    status.name(),
                    completeCheckRule.getId(),
                    allocator.getId(),
                    zeebeJobKey,
                    originator,
                    createTime);
        } else {
            return new TaskEntity(id,
                    "null",
                    this.id,
                    name, priority.name(),
                    status.name(),
                    completeCheckRule.getId(),
                    allocator.getId(),
                    zeebeJobKey,
                    originator,
                    createTime);
        }

    }
    @Override
    public String toString() {
        return "{id : " + id + " name: " + name + " status: " + status +
                "\n executionManager: " + executions.toString() + "}";
    }
}
