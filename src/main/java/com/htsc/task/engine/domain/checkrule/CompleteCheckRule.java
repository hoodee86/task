package com.htsc.task.engine.domain.checkrule;

import com.htsc.task.engine.domain.Task;
import com.htsc.task.engine.repository.dataobj.ConfigDO;

public interface CompleteCheckRule {
    String getId();
    boolean complete(Task task);
    String toJSON();
    void save();
    ConfigDO toDataObject();
}
