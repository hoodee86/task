package tech.nobb.task.engine.protocal.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.nobb.task.engine.protocal.rest.request.CompleteTaskRequest;
import tech.nobb.task.engine.protocal.rest.request.CreateSimpleTaskRequest;
import tech.nobb.task.engine.protocal.rest.request.ClaimTaskRequest;
import tech.nobb.task.engine.protocal.rest.request.CreateAndAssignTaskRequest;
import tech.nobb.task.engine.service.TaskCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.nobb.task.engine.service.TaskExecuteService;

@RestController
@RequestMapping("/api")
public class TaskController {

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskCreateService taskCreateService;
    @Autowired
    TaskExecuteService taskExecuteService;
    @Autowired
    private ObjectMapper mapper;

    // 创建任务，但不分配人
    @RequestMapping(value = "/simple-task", method = RequestMethod.POST)
    public void newSimpleTask(@RequestBody CreateSimpleTaskRequest createSimpleTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(createSimpleTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskCreateService.newSimpleTask(createSimpleTaskRequest);
    }
    // 创建任务，并且分配相关执行人
    @RequestMapping(value = "/task-assign", method = RequestMethod.POST)
    public void newTaskAndAssign(@RequestBody CreateAndAssignTaskRequest createAndAssignTaskRequest) {
        //TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(createAndAssignTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskCreateService.newTaskAndAssign(createAndAssignTaskRequest);
    }

    //某一个人认领一个任务
    @RequestMapping(value = "/claim", method = RequestMethod.POST)
    public void claimTask(@RequestBody ClaimTaskRequest claimTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(claimTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskExecuteService.claimOneTask(claimTaskRequest);
    }

    //完成一个任务
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public void completeTask(@RequestBody CompleteTaskRequest completeTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(completeTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskExecuteService.completeOneTask(completeTaskRequest);
    }

    //暂停一个任务

    //恢复暂停一个任务

    //移交一个任务

}
