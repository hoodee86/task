package tech.nobb.task.engine.protocal.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateSimpleTaskRequest {

    private String name;

    private String checkRule;

    private String threshold;

    private String allocator;

    private long zeebeJobKey;

    private List<String> order;

    private String originator;

}
