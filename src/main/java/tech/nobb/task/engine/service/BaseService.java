package tech.nobb.task.engine.service;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nobb.task.engine.repository.*;

@Service
public class BaseService {
    @Autowired protected TaskRepository taskRepository;
    @Autowired protected ExecutionRepository executionRepository;
    @Autowired protected AllocatorRepository allocatorRepository;
    @Autowired protected TaskViewRepository taskViewRepository;
    @Autowired protected ActionConfigRepository actionConfigRepository;
    @Autowired protected ZeebeClient zeebeClient;
}
