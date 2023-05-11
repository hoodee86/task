package tech.nobb.task.engine.protocal.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AssignTaskRequest {
    private String taskId;
    private List<String> executors;
}
