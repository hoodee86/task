package com.htsc.task.engine.repository;

import com.htsc.task.engine.repository.dataobj.TaskDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskDO, String> {
}
