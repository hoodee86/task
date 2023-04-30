package tech.nobb.task;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
public class TaskEngineApplication {

    public static void main(String[] args) {

        SpringApplication.run(TaskEngineApplication.class, args);
    }

}
