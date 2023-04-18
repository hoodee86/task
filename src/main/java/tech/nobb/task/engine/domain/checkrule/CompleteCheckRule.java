package tech.nobb.task.engine.domain.checkrule;

import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.dataobj.ConfigDO;

public interface CompleteCheckRule {
    String getId();
    boolean complete(Task task);
    String toJSON();
    void save();
    ConfigDO toDataObject();
    void restore();
}
