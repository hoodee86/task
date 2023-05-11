package tech.nobb.task.engine.domain.allocator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;
import tech.nobb.task.engine.repository.AllocatorRepository;
import tech.nobb.task.engine.repository.entity.AllocatorEntity;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ParallelWithPercentageAllocator extends ParallelAllocator {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Logger logger = LoggerFactory.getLogger(ParallelWithPercentageAllocator.class);

    private double percentThreshold;

    public ParallelWithPercentageAllocator(double percentThreshold, AllocatorRepository allocatorRepository) {
        this.id = UUID.randomUUID().toString();
        this.name = "PARALLEL-PERCENTAGE";
        this.percentThreshold = percentThreshold;
        this.allocatorRepository = allocatorRepository;
    }

    public ParallelWithPercentageAllocator(String id, AllocatorRepository allocatorRepository) {
        this.id = id;
        this.allocatorRepository = allocatorRepository;
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

        return completeCount / (double) totalCount >= percentThreshold;
    }

    @Override
    public void save() {
        allocatorRepository.save(toEntity());
    }

    @Override
    public AllocatorEntity toEntity() {
        return new AllocatorEntity(id, name, toJSON());
    }

    @Override
    public void restore() {
        AllocatorEntity allocatorEntity = allocatorRepository.findById(id).orElseGet(null);
        try {
            ParallelWithPercentageAllocator allocator = mapper.readValue(allocatorEntity.getConfig(), ParallelWithPercentageAllocator.class);
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
