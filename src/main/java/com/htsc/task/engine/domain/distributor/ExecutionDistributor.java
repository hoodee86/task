package com.htsc.task.engine.domain.distributor;

import com.htsc.task.engine.domain.Task;

public interface ExecutionDistributor {

    String getId();
    void distribute(Task task);
}
