package tech.nobb.task.engine.domain.allocator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.nobb.task.engine.repository.AllocatorRepository;
import tech.nobb.task.engine.repository.entity.AllocatorEntity;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.Map;
import java.util.UUID;

/**
 * 最通用的并发分发的分配器和完成器，必须并发的全部完成才算完成，默认分配器
 * 允许用户进行再分配，申领任务，转发等等操作
 */

@Data
@NoArgsConstructor
public class ParallelAllocator extends Allocator implements Allocatable, CompleteCheck {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Logger logger = LoggerFactory.getLogger(ParallelAllocator.class);

    public ParallelAllocator(AllocatorRepository allocatorRepository) {
        this.id = UUID.randomUUID().toString();
        this.name = "PARALLEL";
        this.allocatorRepository = allocatorRepository;
    }

    public ParallelAllocator(String id, AllocatorRepository allocatorRepository) {
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

        return completeCount / (double) totalCount >= 1.0;
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
            ParallelAllocator allocator = mapper.readValue(allocatorEntity.getConfig(), ParallelAllocator.class);
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
