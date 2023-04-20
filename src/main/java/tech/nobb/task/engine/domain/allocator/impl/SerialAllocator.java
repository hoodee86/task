package tech.nobb.task.engine.domain.allocator.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigPO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class SerialAllocator implements TaskAllocator {

    private static ObjectMapper mapper = new ObjectMapper();

    private String id;
    private String name;
    private List<String> order;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ConfigRepository configRepository;

    public SerialAllocator(List<String> order, ConfigRepository configRepository) {
        this.order = order;
        id = UUID.randomUUID().toString();
        name = "SERIAL";
        this.configRepository = configRepository;
    }

    public SerialAllocator(ConfigRepository configRepository) {
        id = UUID.randomUUID().toString();
        name = "SERIAL";
        this.order = new ArrayList<>();
        this.configRepository = configRepository;
    }

    public SerialAllocator(String id, ConfigRepository configRepository) {
        this.id = id;
        this.configRepository = configRepository;
        order = new ArrayList<>();
        name = "SERIAL";
    }

    @Override
    public void allocate(Task task) {
        Map<String, Execution> executions = task.getExecutions();
        for (String id : order) {
            Execution e = executions.get(id);
            if (e.getStatus().equals(Execution.Status.CREATED)) {
                e.start();
                e.save();
                break;
            }
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
            SerialAllocator allocator = mapper.readValue(configPO.getProperty(), SerialAllocator.class);
            this.order = allocator.getOrder();
            this.name = allocator.getName();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
