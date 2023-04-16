package com.htsc.task.engine.domain.persist;

import com.htsc.task.engine.domain.Execution;
import com.htsc.task.engine.domain.Handler;

import java.util.List;

public interface HandlerPersist {
    void save(Handler handler);

    void query(String id, Handler handler);

}
