package tech.nobb.task.engine.domain.checkrule.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import tech.nobb.task.engine.domain.checkrule.CompleteCheckRule;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PercentageCheckRule implements CompleteCheckRule {

    private static ObjectMapper mapper = new ObjectMapper();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    Logger logger = LoggerFactory.getLogger(PercentageCheckRule.class);

    private String id;
    private String name;
    private double percentThreshold;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private ConfigRepository configRepository;
    public PercentageCheckRule(double percentThreshold, ConfigRepository configRepository) {
        id = UUID.randomUUID().toString();
        this.percentThreshold = percentThreshold;
        this.name = "PERCENT_CHECK_RULE";
        this.configRepository = configRepository;
    }

    public PercentageCheckRule(String id, ConfigRepository configRepository) {
        this.id = id;
        this.configRepository = configRepository;
    }

    @Override
    public boolean complete(Task task) {
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
    public String toJSON() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save() {
        configRepository.save(toPO());
    }

    @Override
    public ConfigPO toPO() {
        return new ConfigPO(id, "COMPLETE_CHECK_RULE", name, toJSON());
    }

    @Override
    public void restore() {
        ConfigPO configPO = configRepository.findById(id).orElseGet(null);
        try {
            PercentageCheckRule rule = mapper.readValue(configPO.getProperty(), PercentageCheckRule.class);
            this.name = rule.getName();
            this.percentThreshold = rule.getPercentThreshold();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
