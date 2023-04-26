package tech.nobb.task.engine.protocal.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ClaimTaskRequest {
    private String executor;
    private String taskId;
}
