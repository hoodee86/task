package com.htsc.task.engine.repository;

import com.htsc.task.engine.repository.dataobj.ExecutionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionDO, String> {
}
