package com.htsc.task.engine.domain.checkrule.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.task.engine.domain.*;
import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;

import java.util.Map;
import java.util.UUID;

public class PercentageCheckRule implements CompleteCheckRule {

    private String id;
    private String name;
    private final double percentThreshold;
    public PercentageCheckRule(double percentThreshold) {
        id = UUID.randomUUID().toString();
        this.percentThreshold = percentThreshold;
        this.name = "PERCENT";
    }

    @Override
    public boolean isComplete(Task task) {
        Map<Handler, Execution> exeManager = task.getExecutionManager();

        long doneCount = exeManager.keySet().stream()
                                .filter(handler -> exeManager.get(handler).getCurrentStatus().equals(Execution.Status.DONE))
                                .count();

        long totalCount = exeManager.keySet().size();

        System.out.println(doneCount + "-" + totalCount);
        return doneCount / (double) totalCount >= percentThreshold;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPercentThreshold() {
        return percentThreshold;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
