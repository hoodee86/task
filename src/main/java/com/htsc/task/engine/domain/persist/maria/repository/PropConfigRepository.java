package com.htsc.task.engine.domain.persist.maria.repository;


import com.htsc.task.engine.domain.persist.maria.entity.PropConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropConfigRepository extends JpaRepository<PropConfigEntity, String> {
}
