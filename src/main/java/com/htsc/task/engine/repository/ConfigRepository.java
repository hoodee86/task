package com.htsc.task.engine.repository;


import com.htsc.task.engine.repository.dataobj.ConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigDO, String> {
}
