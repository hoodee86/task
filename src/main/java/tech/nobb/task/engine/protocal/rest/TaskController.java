package tech.nobb.task.engine.protocal.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.nobb.task.engine.protocal.rest.dto.SimpleTaskCreateRequest;
import tech.nobb.task.engine.protocal.rest.dto.TaskCreateAndAssignRequest;
import tech.nobb.task.engine.protocal.rest.vo.TaskVO;
import tech.nobb.task.engine.service.TaskCompoundService;
import tech.nobb.task.engine.service.TaskCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskCreateService taskCreateService;
    @Autowired
    TaskCompoundService taskCompoundService;
    @Autowired
    private ObjectMapper mapper;

    @RequestMapping(value = "/simple-task", method = RequestMethod.POST)
    public void newSimpleTask(@RequestBody SimpleTaskCreateRequest simpleTaskCreateRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(simpleTaskCreateRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskCreateService.newSimpleTask(simpleTaskCreateRequest);
    }
    @RequestMapping(value = "/task-assign", method = RequestMethod.POST)
    public void newTaskAndAssign(@RequestBody TaskCreateAndAssignRequest taskCreateAndAssignRequest) {
        //TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(taskCreateAndAssignRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskCreateService.newTaskAndAssign(taskCreateAndAssignRequest);
    }

    @RequestMapping(value = "/task-list-executor", method = RequestMethod.POST)
    public List<TaskVO> taskListByExecutor(String executor) {
        return taskCompoundService.queryTasksByExecutor(executor);
    }

}
