package tech.nobb.task.engine.domain.checkrule;

import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.dataobj.ConfigPO;

public interface CompleteCheckRule {
    String getId();
    boolean complete(Task task);
    String toJSON();
    void save();
    ConfigPO toPO();
    void restore();
}
