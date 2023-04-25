package tech.nobb.task.engine.service;

import org.springframework.stereotype.Service;
import tech.nobb.task.engine.protocal.rest.vo.TaskVO;

import java.util.List;

@Service
public class TaskCompoundService  extends BaseService {
    public List<TaskVO> queryTasksByExecutor(String executor) {
        return taskViewRepository.findTaskVOSByExecutor(executor);
    }

    public List<TaskVO> queryTasksByTaskId(String taskId) {
        return taskViewRepository.findTaskVOsByTaskId(taskId);
    }
}
