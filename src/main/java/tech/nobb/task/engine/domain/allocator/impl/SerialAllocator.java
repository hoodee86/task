package tech.nobb.task.engine.domain.allocator.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.task.engine.domain.*;
import tech.nobb.task.engine.domain.allocator.TaskAllocator;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigDO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class SerialAllocator implements TaskAllocator {

    private String id;
    private String name;
    private final List<String> order;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final ConfigRepository configRepository;

    public SerialAllocator(List<String> order, ConfigRepository configRepository) {
        this.order = order;
        id = UUID.randomUUID().toString();
        name = "SERIAL";
        this.configRepository = configRepository;
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        configRepository.save(toDataObject());
    }

    @Override
    public ConfigDO toDataObject() {
        return new ConfigDO(id, "ALLOCATOR", name, toJSON());
    }
}
