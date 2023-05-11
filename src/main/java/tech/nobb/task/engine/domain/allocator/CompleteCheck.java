package tech.nobb.task.engine.domain.allocator;

import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.entity.AllocatorEntity;

public interface CompleteCheck {
    boolean isComplete(Task task);

}
