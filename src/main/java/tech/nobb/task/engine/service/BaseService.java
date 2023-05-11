package tech.nobb.task.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nobb.task.engine.repository.AllocatorRepository;
import tech.nobb.task.engine.repository.ExecutionRepository;
import tech.nobb.task.engine.repository.TaskRepository;
import tech.nobb.task.engine.repository.TaskViewRepository;

@Service
public class BaseService {
    @Autowired protected TaskRepository taskRepository;
    @Autowired protected ExecutionRepository executionRepository;
    @Autowired protected AllocatorRepository allocatorRepository;
    @Autowired protected TaskViewRepository taskViewRepository;
    @Autowired protected ZeebeClient zeebeClient;
}
