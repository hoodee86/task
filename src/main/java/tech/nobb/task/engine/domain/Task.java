package tech.nobb.task.engine.domain;

import tech.nobb.task.engine.domain.allocator.impl.ParallelAllocator;
import tech.nobb.task.engine.domain.allocator.impl.SerialAllocator;
import tech.nobb.task.engine.domain.checkrule.CompleteCheckRule;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.domain.checkrule.impl.PercentageCheckRule;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.ExecutionRepository;
import tech.nobb.task.engine.repository.TaskRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigPO;
import tech.nobb.task.engine.repository.dataobj.ExecutionPO;
import tech.nobb.task.engine.repository.dataobj.TaskPO;
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
    private Map<String, Execution> executions;
    private Status status;
    private Map<String, Task> subtask;
    private String root;
    private String parent;
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
                String parent,
                CompleteCheckRule completeCheckRule,
                TaskAllocator allocator,
                TaskRepository taskRepository,
                ExecutionRepository executionRepository,
                ConfigRepository configRepository
                ) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.priority = priority;
        this.subtask = new HashMap<String, Task>();
        this.completeCheckRule = completeCheckRule;
        this.allocator = allocator;
        this.executions = new HashMap<String, Execution>();
        this.root = "-1";
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
        this.subtask = new HashMap<>();
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
    public Execution assign(String executor) {
        Execution execution = new Execution(this, executor, executionRepository);
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
            Execution execution = new Execution(this, e, executionRepository);
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
        Execution execution = executions.get(executor);
        execution.forward(otherExecutor);
    }

    // 持久化当前任务对象
    public void save() {
        completeCheckRule.save();
        allocator.save();
        taskRepository.save(toPO());
    }

    // 根据当前对象id还原当前task对象
    public Task restore() {
        return restore(id);
    }

    private Task restore(String id) {
        TaskPO taskPO = taskRepository.findById(id).orElseGet(null);
        if (Objects.isNull(taskPO)) {
            return null;
        } else {
            // 还原当前的task对象
            name = taskPO.getName();
            root = taskPO.getRoot();
            status = Status.valueOf(taskPO.getStatus());
            priority = Priority.valueOf(taskPO.getPriority());
            if ("-1".equals(taskPO.getParent())) {
                parent = "-1";
            } else {
                parent = taskPO.getParent();
            }
            // 还原配置信息
            ConfigPO allocatorPO = configRepository.findById(taskPO.getAllocatorId()).orElseGet(null);
            ConfigPO completeCheckRulePO = configRepository.findById(taskPO.getCheckRuleId()).orElseGet(null);
            if (Objects.isNull(allocator) || Objects.isNull(completeCheckRule)) {
                return null;
            }
            if (allocatorPO.getType().equals("ALLOCATOR")) {
                if (allocatorPO.getName().equals("PARALLEL")) {
                    allocator = new ParallelAllocator(taskPO.getAllocatorId(), configRepository);
                    allocator.restore();
                } else if (allocatorPO.getName().equals("SERIAL")) {
                    allocator = new SerialAllocator(taskPO.getAllocatorId(), configRepository);
                    allocator.restore();
                }
            }
            if (completeCheckRulePO.getType().equals("COMPLETE_CHECK_RULE")) {
                if (completeCheckRulePO.getName().equals("PERCENT_CHECK_RULE")) {
                    completeCheckRule = new PercentageCheckRule(taskPO.getCheckRuleId(), configRepository);
                    allocator.restore();
                }
            }

            // 还原executions
            List<ExecutionPO> executionPOs = executionRepository.findByTaskId(id);
            executionPOs.forEach(
                    executionPO -> {
                        executions.put(executionPO.getExecutorId(), new Execution(
                                executionPO.getTaskId(),
                                executionPO.getExecutorId(),
                                Execution.Status.valueOf(executionPO.getStatus()),
                                this,
                                executionRepository));
                    }
            );

            // 初始化构建subtasks
            List<TaskPO> subtaskPOs = taskRepository.findByParent(taskPO.getId());
            // 递归restore每一个子任务
            subtaskPOs.forEach(
                    subtaskPO -> {
                        subtask.put(subtaskPO.getId(),
                                new Task(subtaskPO.getId(),
                                        taskRepository,
                                        executionRepository,
                                        configRepository).
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

    public TaskPO toPO() {
        if (Objects.nonNull(parent)) {
            return new TaskPO(id,
                    parent,
                    this.id,
                    name, priority.name(),
                    status.name(),
                    completeCheckRule.getId(),
                    allocator.getId());
        } else {
            return new TaskPO(id,
                    "null",
                    this.id,
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
