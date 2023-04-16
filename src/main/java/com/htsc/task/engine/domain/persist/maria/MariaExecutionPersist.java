package com.htsc.task.engine.domain.persist.maria;

import com.htsc.task.engine.domain.Execution;
import com.htsc.task.engine.domain.persist.ExecutionPersist;
import com.htsc.task.engine.domain.persist.maria.repository.ExecutionRepository;
import com.htsc.task.engine.domain.persist.maria.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MariaExecutionPersist implements ExecutionPersist {
    @Autowired private ExecutionRepository executionRepository;

    @Override
    public void save(Execution execution) {
        executionRepository.save(
                new ExecutionEntity(
                    execution.getId(),
                    execution.getHandler().getId(),
                    execution.getTask().getId(),
                    execution.getCurrentStatus().toString()));
    }

    @Override
    public void query(String id, Execution execution) {

    }
}
