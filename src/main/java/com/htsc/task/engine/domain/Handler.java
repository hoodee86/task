package com.htsc.task.engine.domain;

import com.htsc.task.engine.domain.persist.HandlerPersist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 任务处理者
 */
public class Handler {
    private final String id;
    private String name;
    private HandlerPersist persist;
    private List<Execution> box;

    public Handler(String id, String name) {
        this.id = id;
        this.name = name;
        box = new ArrayList<>();
    }

    public Handler(String id, HandlerPersist persist) {
        this.id = id;
        this.persist = persist;
        box = new ArrayList<>();
    }

    public Handler(String id) {
        this.id = id;
        box = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 增加一个execution到当前处理人的执行任务箱中
    public void addOneExecution(Execution execution) {
        if (Objects.nonNull(execution)
                && !box.contains(execution)) {
            box.add(execution);
        }
    }
    // 从当前处理人的执行任务箱中删除一个execution
    public void removeOneExecution(Execution execution) {
        if (Objects.nonNull(execution)) {
            box.remove(execution);
        }
    }
    // 根据execution状态返回当前处理人的任务箱
    public List<Execution> getExecutionBoxByStatus(Execution.Status status) {
        return box.stream().
                filter(execution -> execution.getCurrentStatus().equals(status)).
                collect(Collectors.toList());
    }

    public Execution getExecution(String id) {
        return box.stream()
                  .filter(execution -> execution.getId().equals(id))
                  .collect(Collectors.toList())
                  .get(0);
    }

    public Execution popExecution() {
        if (box.size() > 0) {
            return box.get(box.size() - 1);
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBox(List<Execution> box) {
        this.box = box;
    }

    public void save() {
        persist.save(this);
    }

    public void restore() {
        persist.query(id, this);
    }

    public void printExecutionBox() {
        System.out.println(box.toString());
    }

    @Override
    public String toString() {
        return "{handlerId: " + id + " handlerName: " + name + " executionBoxSize: " + box.size() + "}";
    }

}
