package tech.nobb.task.engine.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "config")
public class ConfigEntity {
    @Id
    private String id;
    @Column(name = "type")
    private String type;
    @Column(name = "name")
    private String name;
    @Column(name = "property")
    private String property;
}
