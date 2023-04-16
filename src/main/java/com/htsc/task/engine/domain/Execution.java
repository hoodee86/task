package com.htsc.task.engine.domain;

import com.htsc.task.engine.domain.persist.ExecutionPersist;

import java.util.UUID;

public class Execution {
    public enum Status {
        // 任务加入到了任务队列中
        ACTIVATED,
        // 任务执行中
        EXECUTING,
        // 任务执行被暂停
        SUSPENDED,
        // 任务执行被完成
        DONE,
        // 任务执行被终止（在一些异常情况下被终止）
        TERMINATED,
        // 任务被取消（正常情况下被取消掉）
        DROPPED
    }

    private Task task; // 这个execution对应的Task
    private final String id; // 任务执行实例自己的ID
    private Status status;
    private Handler handler;
    private ExecutionPersist persist;

    public Execution(Task task, Handler handler) {
        this.task = task;
        this.handler = handler;
        id = UUID.randomUUID().toString();
        status = Status.ACTIVATED;
    }

    public Execution(String id, ExecutionPersist persist) {
        this.id = id;
        this.persist = persist;
    }

    // 获取当前execution的状态
    public Status getCurrentStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public Handler getHandler() {
        return handler;
    }

    public Task getTask() {
        return task;
    }

    // 完成任务
    public void done() {
        if (status.equals(Status.EXECUTING)) {
            status = Status.DONE;
            // 调用task的方法，更新其他整个execution
            task.completeCheck();
            System.out.println("execution is completed");
        } else {
            System.out.println("execution complete error");
        }
    }
    // 暂停任务
    public void suspend() {
        if (status.equals(Status.ACTIVATED)) {
            status = Status.SUSPENDED;
        }
    }
    // 恢复暂停任务
    public void unsuspend() {
        if (status.equals(Status.SUSPENDED)) {
            status = Status.ACTIVATED;
        }
    }

    public void start() {
        status = Status.EXECUTING;
    }

    public void drop() {
        status = Status.DROPPED;
    }

    public void save() {
        persist.save(this);
    }

    public void restore() {
        persist.query(id, this);
    }

    @Override
    public String toString() {
        return "{id: " + id
                + " status: " + status +"}";
    }

}
