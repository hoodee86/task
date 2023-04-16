package com.htsc.task.engine.domain.persist;

import com.htsc.task.engine.domain.Execution;

public interface ExecutionPersist {
    void save(Execution execution);
    void query(String id, Execution execution);
}
