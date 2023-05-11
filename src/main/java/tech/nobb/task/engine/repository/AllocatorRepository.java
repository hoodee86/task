package tech.nobb.task.engine.repository;


import tech.nobb.task.engine.repository.entity.AllocatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllocatorRepository extends JpaRepository<AllocatorEntity, String> {

}
