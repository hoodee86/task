package com.htsc.task.engine.domain.persist.maria;

import com.htsc.task.engine.domain.Task;
import com.htsc.task.engine.domain.persist.CheckRulePersist;
import com.htsc.task.engine.domain.persist.TaskPersist;
import com.htsc.task.engine.domain.persist.maria.entity.TaskEntity;
import com.htsc.task.engine.domain.persist.maria.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MariaTaskPersist implements TaskPersist {

    @Autowired private TaskRepository taskRepository;

    @Autowired private MariaExecutionPersist mariaExecutionPersist;
    @Autowired private MariaCheckRulePersist mariaCheckRulePersist;
    @Autowired private MariaDistributorPersist mariaDistributorPersist;

    @Override
    public void save(Task task) {
        mariaCheckRulePersist.save(task.getCompleteCheckRule());
        mariaDistributorPersist.save(task.getDistributor());
        task.getExecutionManager().values().forEach(e -> {
            mariaExecutionPersist.save(e);
        });
        taskRepository.save(new TaskEntity(
                task.getId(),
                "null",
                task.getName(),
                task.getPriority().name(),
                task.getStatus().name(),
                task.getCompleteCheckRule().getId(),
                task.getDistributor().getId()
        ));
    }

    @Override
    public void query(String id, Task task) {

    }

}
