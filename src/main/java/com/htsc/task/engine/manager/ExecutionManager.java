package com.htsc.task.engine.manager;

import com.htsc.task.engine.domain.persist.ExecutionPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExecutionManager {
    @Autowired private ExecutionPersist executionPersist;


}
