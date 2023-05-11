package tech.nobb.task.engine.domain;

import lombok.Data;
import tech.nobb.task.engine.repository.ActionConfigRepository;
import tech.nobb.task.engine.repository.entity.ActionConfigEntity;
import java.util.Date;
import java.util.UUID;

@Data
public class ActionConfig {

    private String id;
    private boolean assignSubtask;
    private boolean claim;
    private boolean suspend;
    private boolean assign;
    private boolean forward;
    private boolean authorize;
    private final ActionConfigRepository actionConfigRepository;

    public ActionConfig(ActionConfigRepository actionConfigRepository) {
        id = UUID.randomUUID().toString();
        assignSubtask = true;
        claim = true;
        suspend = true;
        assign = true;
        forward = true;
        authorize = true;
        this.actionConfigRepository = actionConfigRepository;
    }

    public ActionConfig(String id, ActionConfigRepository actionConfigRepository) {
        this.id = id;
        this.actionConfigRepository = actionConfigRepository;
        assignSubtask = true;
        claim = true;
        suspend = true;
        assign = true;
        forward = true;
        authorize = true;
    }

    public void save() {
        actionConfigRepository.save(toEntity());
    }

    public ActionConfigEntity toEntity() {
        return new ActionConfigEntity(
                id,
                assignSubtask,
                claim,
                suspend,
                assign,
                forward,
                authorize,
                new Date(),
                new Date());
    }

    public void restore() {
        ActionConfigEntity actionConfigEntity = actionConfigRepository.findById(id).orElseGet(null);
        this.assignSubtask = actionConfigEntity.isAssignSubtask();
        this.claim = actionConfigEntity.isClaim();
        this.assign = actionConfigEntity.isAssign();
        this.forward = actionConfigEntity.isForward();
        this.authorize = actionConfigEntity.isAuthorize();
        this.suspend = actionConfigEntity.isSuspend();
    }

}
