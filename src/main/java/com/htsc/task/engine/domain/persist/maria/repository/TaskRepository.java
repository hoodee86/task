package com.htsc.task.engine.domain.persist.maria.repository;

import com.htsc.task.engine.domain.persist.maria.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, String> {
}
