package tech.nobb.task.engine.domain.checkrule.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.nobb.task.engine.domain.checkrule.CompleteCheckRule;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.dataobj.ConfigDO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.nobb.task.engine.domain.Execution;
import tech.nobb.task.engine.domain.Task;

import java.util.Map;
import java.util.UUID;

@Data
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
    private final ConfigRepository configRepository;
    public PercentageCheckRule(double percentThreshold, ConfigRepository configRepository) {
        id = UUID.randomUUID().toString();
        this.percentThreshold = percentThreshold;
        this.name = "PERCENT_CHECK_RULE";
        this.configRepository = configRepository;
    }

    @Override
    public boolean complete(Task task) {
        Map<String, Execution> executions = task.getExecutions();

        long completeCount = executions.keySet().stream()
                                .filter(id -> executions.get(id).getStatus().equals(Execution.Status.COMPLETED))
                                .count();

        long totalCount = executions.keySet().size();

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
        configRepository.save(toDataObject());
    }

    @Override
    public ConfigDO toDataObject() {
        return new ConfigDO(id, "COMPLETE_CHECK_RULE", name, toJSON());
    }

    @Override
    public void restore() {
        ConfigDO configDO = configRepository.findById(id).orElseGet(null);
        try {
            PercentageCheckRule rule = mapper.readValue(configDO.getProperty(), PercentageCheckRule.class);
            this.name = rule.getName();
            this.percentThreshold = rule.getPercentThreshold();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
