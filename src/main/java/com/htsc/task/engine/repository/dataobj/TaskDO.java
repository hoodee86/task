package com.htsc.task.engine.repository.dataobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class TaskDO {
    @Id
    private String id;
    @Column(name = "parent")
    private String parent;
    @Column(name = "name")
    private String name;
    @Column(name = "priority")
    private String priority;
    @Column(name = "status")
    private String status;
    @Column(name = "check_rule_id")
    private String checkRuleId;
    @Column(name = "allocator_id")
    private String allocatorId;

}
