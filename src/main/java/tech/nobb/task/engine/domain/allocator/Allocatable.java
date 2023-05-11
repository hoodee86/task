package tech.nobb.task.engine.domain.allocator;

import tech.nobb.task.engine.domain.Task;

public interface Allocatable {
    void allocate(Task task);
}
