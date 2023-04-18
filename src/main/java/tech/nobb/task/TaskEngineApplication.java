package tech.nobb.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskEngineApplication {

    public static void main(String[] args) {

        SpringApplication.run(TaskEngineApplication.class, args);
        /*List<String> test = new ArrayList<>();
        test.add("011114");
        test.add("012901");
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(test));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
