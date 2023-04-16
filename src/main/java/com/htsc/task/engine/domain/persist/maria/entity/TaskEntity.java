package com.htsc.task.engine.domain.persist.maria.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class TaskEntity {
    private String id;
    private String parentId;
    private String name;
    private String priority;
    private String status;
    private String checkRuleId;
    private String distributorId;

    public TaskEntity() {

    }

    public TaskEntity(String id,
                      String parentId,
                      String name,
                      String priority,
                      String status,
                      String checkRuleId,
                      String distributorId) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.checkRuleId = checkRuleId;
        this.distributorId = distributorId;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "priority", nullable = false)
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Column(name = "status", nullable = false)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "check_rule_id", nullable = false)
    public String getCheckRuleId() {
        return checkRuleId;
    }

    public void setCheckRuleId(String checkRuleId) {
        this.checkRuleId = checkRuleId;
    }

    @Column(name = "distributor_id", nullable = false)
    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }


}
