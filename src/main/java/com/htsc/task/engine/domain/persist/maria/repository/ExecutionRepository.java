package com.htsc.task.engine.domain.persist.maria.repository;

import com.htsc.task.engine.domain.persist.maria.entity.ExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionEntity, String> {
    public List<ExecutionEntity> findByHandlerId(String id);
}
