package com.htsc.task.engine.domain.allocator;

import com.htsc.task.engine.domain.Task;
import com.htsc.task.engine.repository.dataobj.ConfigDO;

public interface TaskAllocator {

    String getId();
    void allocate(Task task);
    String toJSON();
    void save();
    ConfigDO toDataObject();
}
