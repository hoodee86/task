package tech.nobb.task.engine.protocal.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import tech.nobb.task.engine.protocal.rest.request.*;
import tech.nobb.task.engine.service.TaskAssignService;
import tech.nobb.task.engine.service.TaskCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    TaskAssignService taskAssignService;
    @Autowired
    private ObjectMapper mapper;

    // 创建任务并分配人，如果执行人没指定，则默认为创建人
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public void newSimpleTask(@RequestBody CreateTaskRequest createTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(createTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskCreateService.createTask(createTaskRequest);
    }
    // 给若干人分配任务
    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    public void newTaskAndAssign(@RequestBody AssignTaskRequest assignTaskRequest) {
        //TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(assignTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskAssignService.assignTask(assignTaskRequest.getTaskId(), assignTaskRequest.getExecutors());
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
        taskExecuteService.claimTask(claimTaskRequest);
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
        taskExecuteService.completeTask(completeTaskRequest);
    }

    //暂停一个任务
    @RequestMapping(value = "/suspend", method = RequestMethod.POST)
    public void suspendTask(@RequestBody SuspendTaskRequest suspendTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(suspendTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskExecuteService.suspendTask(suspendTaskRequest);
    }

    //恢复暂停一个任务
    @RequestMapping(value = "/unsuspend", method = RequestMethod.POST)
    public void unsuspendTask(@RequestBody UnsuspendTaskRequest unsuspendTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(unsuspendTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskExecuteService.unsuspendTask(unsuspendTaskRequest);
    }

    //移交一个任务
    @RequestMapping(value = "/forward", method = RequestMethod.POST)
    public void forwardTask(@RequestBody ForwardTaskRequest forwardTaskRequest) {
        // TODO: 对参数进行校验
        try {
            logger.info(mapper.writeValueAsString(forwardTaskRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        taskExecuteService.forwardTask(forwardTaskRequest);
    }
}
