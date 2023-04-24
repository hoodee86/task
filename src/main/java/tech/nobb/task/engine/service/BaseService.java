package tech.nobb.task.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nobb.task.engine.repository.ConfigRepository;
import tech.nobb.task.engine.repository.ExecutionRepository;
import tech.nobb.task.engine.repository.TaskRepository;
import tech.nobb.task.engine.repository.TaskViewDAO;

@Service
public class BaseService {
    @Autowired protected TaskRepository taskRepository;
    @Autowired protected ExecutionRepository executionRepository;
    @Autowired protected ConfigRepository configRepository;
    @Autowired protected TaskViewDAO taskViewDAO;
}
