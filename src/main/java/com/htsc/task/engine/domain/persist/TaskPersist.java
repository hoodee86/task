package com.htsc.task.engine.domain.persist;

import com.htsc.task.engine.domain.Task;

public interface TaskPersist {
    void save(Task task);

    void query(String id, Task task);
}
