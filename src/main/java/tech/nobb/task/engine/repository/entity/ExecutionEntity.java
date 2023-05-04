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
@Table(name = "execution")
public class ExecutionEntity {
    @Id
    private String id;
    @Column(name = "executor_id", nullable = false)
    private String executorId;
    @Column(name = "forward_id")
    private String forwardId;
    @Column(name = "task_id", nullable = false)
    private String taskId;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "create_time")
    private Date createTime;
}
