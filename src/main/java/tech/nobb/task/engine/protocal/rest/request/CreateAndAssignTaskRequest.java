package tech.nobb.task.engine.protocal.rest.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateAndAssignTaskRequest {
    private String name;
    private String checkRule;
    private String threshold;
    private String allocator;
    private long zeebeJobKey;
    private List<String> order;
    private List<String> executors;
    private String originator;
}
