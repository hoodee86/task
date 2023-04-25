package tech.nobb.task.engine.protocal.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.nobb.task.engine.protocal.rest.vo.TaskVO;
import tech.nobb.task.engine.service.TaskCompoundService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskQueryController {
    private Logger logger = LoggerFactory.getLogger(TaskQueryController.class);

    @Autowired
    TaskCompoundService taskCompoundService;
    @Autowired
    private ObjectMapper mapper;

    // 根据某一个执行人的ID筛选出这个人下面的任务列表
    @RequestMapping(value = "/task-list-executor", method = RequestMethod.POST)
    public List<TaskVO> taskListByExecutor(String executor) {
        return taskCompoundService.queryTasksByExecutor(executor);
    }

    // 根据某一个任务ID筛选出其下所有的执行实例信息
    @RequestMapping(value = "/task-list-id", method = RequestMethod.POST)
    public List<TaskVO> taskListByTaskId(String taskId) {
        return taskCompoundService.queryTasksByTaskId(taskId);
    }

}
