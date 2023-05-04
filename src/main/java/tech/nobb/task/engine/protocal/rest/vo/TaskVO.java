package tech.nobb.task.engine.protocal.rest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date taskCreateTime;
    private Date executionCreateTime;
    private String originator;

    public TaskVO(String taskId, String executionId, String taskName, String priority, String taskStatus, String executionStatus, String executor, Date taskCreateTime, Date executionCreateTime, String originator) {
        this.taskId = taskId;
        this.executionId = executionId;
        this.taskName = taskName;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.executionStatus = executionStatus;
        this.executor = executor;
        this.taskCreateTime = taskCreateTime;
        this.executionCreateTime = executionCreateTime;
        this.originator = originator;
    }
}
