package com.htsc.task.engine.domain.distributor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.task.engine.domain.*;
import com.htsc.task.engine.domain.distributor.ExecutionDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParallelDistributor implements ExecutionDistributor {

    private String id;
    private String name;

    public ParallelDistributor() {
        this.id = UUID.randomUUID().toString();
        this.name = "PARALLEL";
    }

    @Override
    public void distribute(Task task) {
        Map<Handler, Execution> executionManager = task.getExecutionManager();
        List<Handler> handlers = new ArrayList<>(executionManager.keySet());
        handlers.forEach(handler -> {
            // 将每个execution的引用放在handler的处理队列中，并设置状态为EXECUTING
            Execution exe = executionManager.get(handler);
            if (exe.getCurrentStatus().equals(Execution.Status.ACTIVATED)) {
                handler.addOneExecution(exe);
                exe.start();
            }
        });
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
