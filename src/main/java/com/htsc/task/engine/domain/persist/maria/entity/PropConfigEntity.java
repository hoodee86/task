package com.htsc.task.engine.domain.persist.maria.entity;

import javax.persistence.*;

@Entity
@Table(name = "prop_config")
public class PropConfigEntity {
    private String id;
    private String propType;
    private String configType;
    private String property;

    public PropConfigEntity() { }

    public PropConfigEntity(String id, String propType, String configType, String property) {
        this.id = id;
        this.propType = propType;
        this.configType = configType;
        this.property = property;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "prop_type", nullable = false)
    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType;
    }

    @Column(name = "config_type", nullable = false)
    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    @Column(name = "property")
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
