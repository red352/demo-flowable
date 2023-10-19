package org.example.config;

import org.example.process.SfmProcess;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author LuoYunXiao
 * @since 2023/10/11 16:56
 */
@Configuration
public class FlowAbleConfig {


    /*
    模拟流程
     */
    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService) {

        return strings -> {


            repositoryService.createDeploymentQuery()
                    .list().forEach(deployment -> repositoryService.deleteDeployment(deployment.getId(), true));

            repositoryService.createDeployment()
                    .addBpmnModel("processes/holiday-request.bpmn20.xml", SfmProcess.create())
                    .key("holiday-java-deploy")
                    .name("测试使用java部署")
                    .deploy();


            Map<String, Object> variables;
            // 职员提交审批
            Scanner scanner = new Scanner(System.in);

            while (!Thread.interrupted()) {
                // 选择角色
                System.out.println("请选择角色，0：职员，1：管理者");
                switch (scanner.nextLine()) {
                    case "0" -> {
                        System.out.println("Who are you?");
                        String employee = scanner.nextLine();
                        System.out.println("How many holidays do you want to request?");
                        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());
                        System.out.println("Why do you need them?");
                        String description = scanner.nextLine();
                        variables = new HashMap<>();
                        variables.put("employee", employee);
                        variables.put("nrOfHolidays", nrOfHolidays);
                        variables.put("description", description);
                        runtimeService.startProcessInstanceByKey("holiday-java", variables);
                    }
                    case "1" -> {
                        // 管理者进入审批界面
                        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
                        System.out.println("You have " + tasks.size() + " tasks:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + ") " + tasks.get(i).getName());
                        }

                        System.out.println("Which task would you like to complete?");
                        int taskIndex = Integer.parseInt(scanner.nextLine());
                        Task task = tasks.get(taskIndex - 1);
                        Map<String, Object> processVariables = taskService.getVariables(task.getId());
                        System.out.println(processVariables.get("employee") + " wants " +
                                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

                        // 审批结果
                        boolean approved = scanner.nextLine().equalsIgnoreCase("y");
                        variables = new HashMap<>();
                        variables.put("approved", approved);
                        taskService.complete(task.getId(), variables);
                    }
                }
            }


        };
    }

}
