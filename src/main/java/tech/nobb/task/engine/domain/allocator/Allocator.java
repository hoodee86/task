package tech.nobb.task.engine.domain.allocator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.nobb.task.engine.repository.AllocatorRepository;
import tech.nobb.task.engine.repository.entity.AllocatorEntity;

@Data
public abstract class Allocator implements Allocatable, CompleteCheck {
    protected String id;
    protected String name;
    protected static ObjectMapper mapper = new ObjectMapper();
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected AllocatorRepository allocatorRepository;
    abstract public String toJSON();
    abstract public void save();
    abstract public void restore();
    abstract public AllocatorEntity toEntity();
}
