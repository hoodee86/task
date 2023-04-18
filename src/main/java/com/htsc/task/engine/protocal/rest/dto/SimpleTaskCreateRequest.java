package com.htsc.task.engine.protocal.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleTaskCreateRequest {

    private String name;

    private String checkRule;

    private String allocator;

}
