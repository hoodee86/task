package com.htsc.task.engine.domain.persist.maria;

import com.htsc.task.engine.domain.Handler;
import com.htsc.task.engine.domain.persist.HandlerPersist;
import com.htsc.task.engine.domain.persist.maria.repository.HandlerRepository;
import com.htsc.task.engine.domain.persist.maria.entity.HandlerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MariaHandlerPersist implements HandlerPersist {

    @Autowired
    private HandlerRepository handlerRepository;


    @Override
    public void save(Handler handler) {
        handlerRepository.save(new HandlerEntity(handler.getId(), handler.getName()));
    }

    @Override
    public void query(String id, Handler handler) {
        HandlerEntity handlerEntity = handlerRepository.findById(id).orElse(null);
    }

}
