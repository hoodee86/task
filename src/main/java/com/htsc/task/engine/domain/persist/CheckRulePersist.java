package com.htsc.task.engine.domain.persist;

import com.htsc.task.engine.domain.Execution;
import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;

public interface CheckRulePersist {
    void save(CompleteCheckRule checkRule);

    void query(String id, CompleteCheckRule checkRule);
}
