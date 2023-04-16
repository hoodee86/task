package com.htsc.task.engine.domain;

import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;
import com.htsc.task.engine.domain.distributor.ExecutionDistributor;
import com.htsc.task.engine.domain.persist.TaskPersist;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Task {
    public enum Status {
        // 任务被创建
        CREATED,
        // 任务被认领
        CLAIMED,
        // 任务被取消
        CANCELED,
        // 任务完成
        COMPLETED
    }

    public enum Priority {
        HIGH,
        MIDDLE,
        LOW
    }

    private final String id;
    private String name;
    private Priority priority;
    private Map<Handler, Execution> executionManager;
    private Status status;
    private List<Task> subtask;
    private Task parent;
    private CompleteCheckRule completeCheckRule;
    private ExecutionDistributor distributor;
    private TaskPersist persist;

    public Task(String name,
                Priority priority,
                CompleteCheckRule completeCheckRule,
                ExecutionDistributor distributor,
                List<Handler> handlers,
                TaskPersist persist,
                Task parent) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.priority = priority;
        this.subtask = new ArrayList<>();
        this.completeCheckRule = completeCheckRule;
        this.distributor = distributor;
        this.executionManager = new HashMap<>();
        this.parent = parent;
        this.persist = persist;
        this.priority = Priority.LOW;
        handlers.forEach(handler -> executionManager.put(
                handler,
                new Execution(this, handler)));
        distributor.distribute(this);
        this.status = Status.CLAIMED;
    }

    public Task(String id, TaskPersist persist) {
        this.id = id;
        this.persist = persist;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public Task getParent() {
        return parent;
    }

    public CompleteCheckRule getCompleteCheckRule() {
        return completeCheckRule;
    }

    public Map<Handler, Execution> getExecutionManager() {
        return executionManager;
    }

    public ExecutionDistributor getDistributor() {
        return distributor;
    }

    // execution每当执行done的时候，都会去调用该方法，以检查当前Task是否做完
    // 此方法会调用completeCheckRule的具体实例
    public void completeCheck() {
        if (completeCheckRule.isComplete(this)) {
            // 将当前任务的状态设置成COMPLETED
            status = Status.COMPLETED;
            // 将TASK进行扫尾
            doCompleted();
        } else {
            distributor.distribute(this);
        }
    }

    public void save() {
        persist.save(this);
    }

    private void doCompleted() {
        if (status.equals(Status.COMPLETED)) {
            // 将当前TASK的所有ACTIVATED，EXECUTING，SUSPENDED状态置成DROPPED
            executionManager.forEach((handler, execution) -> {
                if (execution.getCurrentStatus().equals(Execution.Status.ACTIVATED) ||
                    execution.getCurrentStatus().equals(Execution.Status.EXECUTING) ||
                    execution.getCurrentStatus().equals(Execution.Status.SUSPENDED)) {
                    execution.drop();
                }
            });
        }
    }

    @Override
    public String toString() {
        return "{id : " + id + " name: " + name + " status: " + status +
                "\n executionManager: " + executionManager.toString() + "}";
    }
}
