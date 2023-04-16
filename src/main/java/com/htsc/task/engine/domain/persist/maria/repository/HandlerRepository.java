package com.htsc.task.engine.domain.persist.maria.repository;

import com.htsc.task.engine.domain.persist.maria.entity.HandlerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandlerRepository extends JpaRepository<HandlerEntity, String> {
}
