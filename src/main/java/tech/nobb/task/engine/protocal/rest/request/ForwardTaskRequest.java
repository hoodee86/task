package tech.nobb.task.engine.protocal.rest.request;

import lombok.Data;

@Data
public class ForwardTaskRequest {
    private String taskId;
    private String executor;
    private String other;
}
