package tech.nobb.task.engine.domain.allocator.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigDO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.Map;
import java.util.UUID;
@Data
public class ParallelAllocator implements TaskAllocator {

    private static ObjectMapper mapper = new ObjectMapper();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ConfigRepository configRepository;

    private String id;
    private String name;

    public ParallelAllocator(ConfigRepository configRepository) {
        this.id = UUID.randomUUID().toString();
        this.name = "PARALLEL";
        this.configRepository = configRepository;
    }

    @Override
    public void allocate(Task task) {
        Map<String, Execution> executions = task.getExecutions();
        executions.forEach((id, execution) -> {
            if (execution.getStatus().equals(Execution.Status.CREATED)) {
                execution.start();
                execution.save();
            }
        });
    }

    @Override
    public void save() {
        configRepository.save(toDataObject());
    }

    @Override
    public ConfigDO toDataObject() {
        return new ConfigDO(id, "ALLOCATOR", name, toJSON());
    }

    @Override
    public void restore() {
        ConfigDO configDO = configRepository.findById(id).orElseGet(null);
        try {
            ParallelAllocator allocator = mapper.readValue(configDO.getProperty(), ParallelAllocator.class);
            this.name = allocator.getName();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJSON() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}