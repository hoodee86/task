package com.htsc.task.engine.protocal.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.task.engine.protocal.rest.dto.TaskCreateDTO;
import com.htsc.task.engine.manager.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskManager taskManager;

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public void createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        // TODO: 需要对传进来的参数进行校验，看是否存在非法参数
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(taskCreateDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        taskManager.createTask(taskCreateDTO);
    }

}
