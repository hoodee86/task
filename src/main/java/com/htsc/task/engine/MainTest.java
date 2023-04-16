package com.htsc.task.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.task.engine.protocal.rest.dto.TaskCreateDTO;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("aaaa");
        dto.setCheckRuleType("bbb");
        dto.setDistributorType("cccc");
        List<String> handlers = new ArrayList<>();
        handlers.add("011114");
        handlers.add("012901");
        dto.setHandlers(handlers);
        try {
            System.out.println(mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    private static void printHandlerBox(List<Handler> handlers) {
        System.out.println();
        System.out.println("打印每个处理人的任务队列与执行任务队列");
        handlers.forEach(handler -> {
            System.out.println("Handler : " + handler.toString());
            System.out.print("Queue: ");
            handler.printExecutionBox();
        });
        System.out.println();
    }*/

    /*

    public static void main(String[] args) {
        Handler caoxiao = new Handler("011114", "caoxiao");//caoxiao
        Handler luxiao = new Handler("012901", "luxiao");//luxiao
        Handler wangding = new Handler("020047", "wangding");//wangding
        Handler zhangchen = new Handler("015754", "zhangchen");//zhangchen

        List<Handler> handlers1 = new ArrayList<>();
        handlers1.add(caoxiao);
        handlers1.add(luxiao);
        handlers1.add(wangding);
        handlers1.add(zhangchen);

        List<Handler> handlers2 = new ArrayList<>();
        handlers2.add(caoxiao);
        handlers2.add(luxiao);
        handlers2.add(wangding);

        System.out.println("给四个人创建任务A");
        // 给任务1配置一个规则

        PercentageCheckRule percentageCheckRule =
                new PercentageCheckRule(0.5);

        ParallelDistributor parallelDistributor = new ParallelDistributor();
        SerialDistributor serialDistributor = new SerialDistributor(handlers2);

        Task taska = new Task("任务A", null, percentageCheckRule, parallelDistributor, handlers1, null);

        System.out.println(taska.toString());

        printHandlerBox(handlers1);

        System.out.println("给3个人创建任务B, caoxiao, luxiao, wangding:");

        Task taskb = new Task("任务B", null, percentageCheckRule, serialDistributor, handlers2, null);

        System.out.println(taskb);
        printHandlerBox(handlers1);

        caoxiao.popExecution().done();
        System.out.println(taskb);
        printHandlerBox(handlers1);

        luxiao.popExecution().done();
        System.out.println(taskb);
        printHandlerBox(handlers1);

        wangding.popExecution().done();
        System.out.println(taskb);
        printHandlerBox(handlers1);




        System.out.println("caoxiao claim task1:");
        Execution execution_cx_1 = caoxiao.claimTask(humanTask1);

        printQueue(assignees);

        System.out.println("luxiao, wangding, zhangchen claim task1:");
        //TaskExecution taskExecution_cx_2 = caoxiao.claimTask(humanTask2);
        Execution execution_lx_1 = luxiao.claimTask(humanTask1);
        //TaskExecution taskExecution_lx_2 = luxiao.claimTask(humanTask2);
        Execution execution_wd_1 = wangding.claimTask(humanTask1);
        Execution execution_zc_1 = zhangchen.claimTask(humanTask1);

        printQueue(assignees);

        System.out.println("caoxiao,wangding,luxiao,zhangchen start completing humantask 1");
        System.out.println("caoxiao complete humantask1");
        caoxiao.completeTask(execution_cx_1);
        System.out.println(humanTask1.getTaskStatus());
        System.out.println("luxiao complete humantask1");
        luxiao.completeTask(execution_lx_1);
        System.out.println(humanTask1.getTaskStatus());
        System.out.println("wangding complete humantask1");
        wangding.completeTask(execution_wd_1);
        System.out.println(humanTask1.getTaskStatus());
        System.out.println("zhangchen complete humantask1");
        zhangchen.completeTask(execution_zc_1);
        System.out.println(humanTask1.getTaskStatus());

        printQueue(assignees);
    }*/
}
