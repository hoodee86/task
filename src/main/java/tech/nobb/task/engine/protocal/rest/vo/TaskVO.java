package tech.nobb.task.engine.protocal.rest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskVO {
    private String taskId;
    private String executionId;
    private String taskName;
    private String priority;
    private String taskStatus;
    private String executionStatus;
    private String executor;

    public TaskVO(String taskId, String executionId, String taskName, String priority, String taskStatus, String executionStatus, String executor) {
        this.taskId = taskId;
        this.executionId = executionId;
        this.taskName = taskName;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.executionStatus = executionStatus;
        this.executor = executor;
    }
}
