package tech.nobb.task.engine.protocal.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimpleTaskCreateRequest {

    private String name;

    private String checkRule;

    private String threshold;

    private String allocator;

    private List<String> order;

}
