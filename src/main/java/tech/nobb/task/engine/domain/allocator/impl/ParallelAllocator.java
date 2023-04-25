package tech.nobb.task.engine.domain.allocator.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigPO;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.Map;
import java.util.UUID;
@Data
@NoArgsConstructor
public class ParallelAllocator implements TaskAllocator {

    private static ObjectMapper mapper = new ObjectMapper();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private ConfigRepository configRepository;

    private String id;
    private String name;

    public ParallelAllocator(ConfigRepository configRepository) {
        this.id = UUID.randomUUID().toString();
        this.name = "PARALLEL";
        this.configRepository = configRepository;
    }

    public ParallelAllocator(String id, ConfigRepository configRepository) {
        this.id = id;
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
        configRepository.save(toPO());
    }

    @Override
    public ConfigPO toPO() {
        return new ConfigPO(id, "ALLOCATOR", name, toJSON());
    }

    @Override
    public void restore() {
        ConfigPO configPO = configRepository.findById(id).orElseGet(null);
        try {
            ParallelAllocator allocator = mapper.readValue(configPO.getProperty(), ParallelAllocator.class);
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
