package tech.nobb.task.engine.domain.allocator;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.nobb.task.engine.repository.AllocatorRepository;
import tech.nobb.task.engine.repository.entity.AllocatorEntity;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.*;

@Data
@NoArgsConstructor
public class SerialAllocator extends Allocator implements Allocatable, CompleteCheck {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected Logger logger = LoggerFactory.getLogger(SerialAllocator.class);

    private List<String> order;

    public SerialAllocator(List<String> order, AllocatorRepository allocatorRepository) {
        this.order = order;
        id = UUID.randomUUID().toString();
        name = "SERIAL";
        this.allocatorRepository = allocatorRepository;
    }

    public SerialAllocator(AllocatorRepository allocatorRepository) {
        id = UUID.randomUUID().toString();
        name = "SERIAL";
        this.order = new ArrayList<>();
        this.allocatorRepository = allocatorRepository;
    }

    public SerialAllocator(String id, AllocatorRepository allocatorRepository) {
        this.id = id;
        this.allocatorRepository = allocatorRepository;
        order = new ArrayList<>();
        name = "SERIAL";
    }

    @Override
    public void allocate(Task task) {
        Map<String, Execution> executions = task.getExecutions();
        for (String eid : order) {
            Execution e = executions.get(eid);
            if (Objects.nonNull(e) && e.getStatus().equals(Execution.Status.CREATED)) {
                e.start();
                e.save();
                break;
            }
        }
    }

    @Override
    public boolean isComplete(Task task) {
        Map<String, Execution> executions = task.getExecutions();

        long completeCount = executions.keySet().stream()
                .filter(id -> executions.get(id).getStatus().equals(Execution.Status.COMPLETED))
                .count();

        /*long totalCount = executions.keySet().stream()
                                .filter(id -> !executions.get(id).getStatus().equals(Execution.Status.FORWARDED))
                                .count();*/
        long totalCount = executions.size();

        logger.info(completeCount + "-" + totalCount);

        return completeCount / (double) totalCount >= 1.0;
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
        allocatorRepository.save(toEntity());
    }

    @Override
    public AllocatorEntity toEntity() {
        return new AllocatorEntity(id,  name, toJSON());
    }

    @Override
    public void restore() {
        AllocatorEntity allocatorEntity = allocatorRepository.findById(id).orElseGet(null);
        try {
            SerialAllocator allocator = mapper.readValue(allocatorEntity.getConfig(), SerialAllocator.class);
            this.order = allocator.getOrder();
            this.name = allocator.getName();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
