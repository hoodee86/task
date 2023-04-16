package com.htsc.task.engine.domain.distributor.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.task.engine.domain.*;
import com.htsc.task.engine.domain.distributor.ExecutionDistributor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SerialDistributor implements ExecutionDistributor {

    private String id;
    private String name;
    private final List<Handler> order;

    public SerialDistributor(List<Handler> order) {
        this.order = order;
        id = UUID.randomUUID().toString();
        name = "SERIAL";
    }

    @Override
    public void distribute(Task task) {
        Map<Handler, Execution> executionManager = task.getExecutionManager();
        for (Handler handler : order) {
            if (executionManager.get(handler).getCurrentStatus().equals(Execution.Status.ACTIVATED)) {
                handler.addOneExecution(executionManager.get(handler));
                executionManager.get(handler).start();
                break;
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Handler> getOrder() {
        return order;
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
