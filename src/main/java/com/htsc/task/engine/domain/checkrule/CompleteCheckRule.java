package com.htsc.task.engine.domain.checkrule;

import com.htsc.task.engine.domain.Task;

public interface CompleteCheckRule {
    String getId();
    boolean isComplete (Task task);
}
