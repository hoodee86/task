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
@Table(name = "execution")
public class ExecutionDO {
    @Id
    private String id;
    @Column(name = "executor_id")
    private String executorId;
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "status")
    private String status;

}
