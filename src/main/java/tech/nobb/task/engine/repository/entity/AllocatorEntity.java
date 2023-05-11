package tech.nobb.task.engine.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "allocator")
public class AllocatorEntity {
    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "config")
    private String config;
}
