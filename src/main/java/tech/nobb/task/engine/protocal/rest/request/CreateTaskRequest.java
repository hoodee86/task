package tech.nobb.task.engine.protocal.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    private String name;
    private long zeebeJobKey;
    private String originator;
    private String allocator;
    private Map<String, Object> allocatorConfig;     // JSON format
    private List<String> executors;                   // 如果没有指定，则默认等同于originator
}
