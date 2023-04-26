package tech.nobb.task.engine.protocal.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CompleteTaskRequest {
    private String executor;
    private String taskId;
}
