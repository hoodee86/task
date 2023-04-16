package com.htsc.task.engine.domain.persist.maria;

import com.htsc.task.engine.domain.distributor.ExecutionDistributor;
import com.htsc.task.engine.domain.distributor.impl.ParallelDistributor;
import com.htsc.task.engine.domain.distributor.impl.SerialDistributor;
import com.htsc.task.engine.domain.persist.DistributorPersist;
import com.htsc.task.engine.domain.persist.maria.repository.PropConfigRepository;
import com.htsc.task.engine.domain.persist.maria.entity.PropConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MariaDistributorPersist implements DistributorPersist {

    @Autowired
    private PropConfigRepository propConfigRepository;

    @Override
    public void save(ExecutionDistributor distributor) {
        if (distributor instanceof ParallelDistributor) {
            ParallelDistributor parallelDistributor = (ParallelDistributor) distributor;
            PropConfigEntity entity = new PropConfigEntity(
                    parallelDistributor.getId(),
                    "DISTRIBUTOR",
                    parallelDistributor.getName(),
                    parallelDistributor.toString());
            propConfigRepository.save(entity);
        } else if (distributor instanceof SerialDistributor) {
            SerialDistributor serialDistributor = (SerialDistributor) distributor;
            PropConfigEntity entity = new PropConfigEntity(
                    serialDistributor.getId(),
                    "DISTRIBUTOR",
                    serialDistributor.getName(),
                    serialDistributor.toString());
            propConfigRepository.save(entity);
        }
    }

    @Override
    public void query(String id, ExecutionDistributor distributor) {

    }
}
