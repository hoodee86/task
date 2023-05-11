package tech.nobb.task.engine.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "action_config")
public class ActionConfigEntity {
    @Id
    private String actionConfigId;
    @Column(name = "assign_subtask")
    private boolean assignSubtask;
    @Column(name = "claim")
    private boolean claim;
    @Column(name = "suspend")
    private boolean suspend;
    @Column(name = "assign")
    private boolean assign;
    @Column(name = "forward")
    private boolean forward;
    @Column(name = "authorize")
    private boolean authorize;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
}
