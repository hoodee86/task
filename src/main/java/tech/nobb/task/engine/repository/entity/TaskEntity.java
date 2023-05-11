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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class TaskEntity {
    @Id
    private String id;
    @Column(name = "parent", nullable = false)
    private String parent;
    @Column(name = "root", nullable = false)
    private String root;
    @Column(name = "name")
    private String name;
    @Column(name = "priority")
    private String priority;
    @Column(name = "status")
    private String status;
    @Column(name = "allocator_id")
    private String allocatorId;
    @Column(name = "zeebe_job_key")
    private long zeebeJobKey;
    @Column(name = "originator")
    private String originatorId;
    @Column(name = "create_time")
    private Date createTime;
}
