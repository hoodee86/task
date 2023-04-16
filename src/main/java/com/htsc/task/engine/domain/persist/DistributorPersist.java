package com.htsc.task.engine.domain.persist;

import com.htsc.task.engine.domain.Execution;
import com.htsc.task.engine.domain.distributor.ExecutionDistributor;

public interface DistributorPersist {
    void save(ExecutionDistributor distributor);

    void query(String id, ExecutionDistributor distributor);
}
