package tech.nobb.task.engine.domain.allocator;

import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.dataobj.ConfigDO;

public interface TaskAllocator {

    String getId();
    void allocate(Task task);
    String toJSON();
    void save();
    ConfigDO toDataObject();
}
