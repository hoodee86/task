package com.htsc.task.engine.protocal.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskCreateDTO {

    private String name;

    private List<String> handlers;

    private String checkRuleType;

    private String distributorType;

}
