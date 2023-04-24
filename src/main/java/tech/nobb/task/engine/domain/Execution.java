package tech.nobb.task.engine.domain;

import tech.nobb.task.engine.repository.ExecutionRepository;
import tech.nobb.task.engine.repository.dataobj.ExecutionPO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
public class Execution {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Logger logger = LoggerFactory.getLogger(Execution.class);

    public enum Status {
        // 加入到了任务队列中，初始化
        CREATED,
        // 任务执行中
        EXECUTING,
        // 任务执行被暂停
        SUSPENDED,
        // 任务执行被完成
        COMPLETED,
        // 任务执行被终止（在一些异常情况下被终止）
        TERMINATED,
        // 任务被取消（正常情况下被取消掉）
        DROPPED
    }
    private final String id; // 任务执行实例自己的ID
    private Task task; // 这个execution对应的Task
    private Status status;
    private String executor;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private ExecutionRepository executionRepository;

    public Execution(Task task, String executor, ExecutionRepository executionRepository) {
        id = UUID.randomUUID().toString();
        this.task = task;
        this.executor = executor;
        this.executionRepository = executionRepository;
        status = Status.CREATED;
    }

    public Execution(String id, String executor, Status status, Task task, ExecutionRepository executionRepository) {
        this.id = id;
        this.task = task;
        this.status = status;
        this.executor = executor;
        this.executionRepository = executionRepository;
    }

    // 当前实例执行完成
    public void complete() {
        if (status.equals(Status.EXECUTING)) {
            status = Status.COMPLETED;
            // 调用task的方法，更新其他整个execution
            task.checkCompletion();
            logger.info("execution is completed");
        } else {
            logger.info("execution complete error");
        }
    }
    // 暂停任务
    public void suspend() {
        if (status.equals(Status.EXECUTING)) {
            status = Status.SUSPENDED;
        }
    }
    // 恢复暂停任务
    public void unsuspend() {
        if (status.equals(Status.SUSPENDED)) {
            status = Status.EXECUTING;
        }
    }
    // 开始一个任务
    public void start() {
        if (status.equals(Status.CREATED)) {
            status = Status.EXECUTING;
        }
    }
    // 丢弃一个任务
    public void drop() {
        if (!status.equals(Status.COMPLETED)) {
            status = Status.DROPPED;
        }
    }
    // 终止任务
    public void terminate() {
        if (status.equals(Status.EXECUTING)) {
            status = Status.TERMINATED;
        }
    }
    // 转办任务
    public void forward(String other) {
        if (status.equals(Status.EXECUTING)) {
            executor = other;
        }
    }
    public void save() {
        executionRepository.save(this.toPO());
    }

    /*
    // ！！！注意：execution对象的还原无法单独还原，必须通过其Task对象进行还原。
    public void restore(Task task) {
        ExecutionPO executionPO = executionRepository.findById(id).orElseGet(null);
        if (task.getId().equals(executionPO.getTaskId())) {
            this.executor = executionPO.getExecutorId();
            this.task = task;
            this.status = Status.valueOf(executionPO.getStatus());
        }
    }*/

    public ExecutionPO toPO() {
        return new ExecutionPO(
                id,
                executor,
                task.getId(),
                status.name()
        );
    }

    @Override
    public String toString() {
        return "{id: " + id
                + " status: " + status +"}";
    }

}
