package com.htsc.task.engine.manager;

import com.htsc.task.engine.domain.*;
import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;
import com.htsc.task.engine.domain.checkrule.impl.PercentageCheckRule;
import com.htsc.task.engine.domain.distributor.ExecutionDistributor;
import com.htsc.task.engine.domain.distributor.impl.ParallelDistributor;
import com.htsc.task.engine.domain.distributor.impl.SerialDistributor;
import com.htsc.task.engine.domain.persist.TaskPersist;
import com.htsc.task.engine.protocal.rest.dto.TaskCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class TaskManager {

    @Autowired private TaskPersist persist;

    public void createTask(TaskCreateDTO taskCreateDTO) {

        // 先构建处理者
        List<Handler> handlers = taskCreateDTO.getHandlers().stream()
                .map(Handler::new)
                .collect(Collectors.toList());
        CompleteCheckRule checkRule = null;
        ExecutionDistributor distributor = null;
        if ("percentage".equals(taskCreateDTO.getCheckRuleType())) {
            checkRule = new PercentageCheckRule(0.75);
        }
        if ("parallel".equals(taskCreateDTO.getDistributorType())) {
            distributor = new ParallelDistributor();
        }
        if ("serial".equals(taskCreateDTO.getDistributorType())) {
            distributor = new SerialDistributor(handlers);
        }

        if (Objects.nonNull(checkRule) && Objects.nonNull(distributor)) {
            Task task = new Task(
                    taskCreateDTO.getName(),
                    Task.Priority.LOW,
                    checkRule,
                    distributor,
                    handlers,
                    persist,
                    null
            );

            persist.save(task);
        }
    }
}
