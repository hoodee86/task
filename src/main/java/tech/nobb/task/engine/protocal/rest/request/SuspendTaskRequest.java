package tech.nobb.task.engine.protocal.rest.request;

import lombok.Data;

@Data
public class SuspendTaskRequest {
    private String executor;
    private String taskId;
}
