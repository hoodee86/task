package tech.nobb.task.engine.domain.config;

public interface ActionConfig {
    boolean canAssignSubtask();
    boolean canClaim();
    boolean canSuspend();
    boolean canReassign();
    boolean canForward();
    boolean canAuthorize();
}
