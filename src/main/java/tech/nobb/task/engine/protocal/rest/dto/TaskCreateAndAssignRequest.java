package tech.nobb.task.engine.protocal.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskCreateAndAssignRequest {
    private String name;
    private String checkRule;
    private String threshold;
    private String allocator;
    private List<String> order;
    private List<String> executors;
}
