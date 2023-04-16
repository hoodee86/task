package com.htsc.task.engine.domain.persist.maria.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "execution")
public class ExecutionEntity {
    private String id;
    private String handlerId;
    private String taskId;
    private String status;

    public ExecutionEntity() {

    }
    public ExecutionEntity(String id, String handlerId, String taskId, String status) {
        this.id = id;
        this.handlerId = handlerId;
        this.taskId = taskId;
        this.status = status;
    }
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "handler_id", nullable = false)
    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    @Column(name = "task_id", nullable = false)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    @Column(name = "status", nullable = false)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
