package com.learn.springcloud.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Descritption TODO
 * @Author shunzhong.deng
 * @Date 2018/10/18 18:29
 * @Version 1.0
 **/
@SpringBootTest
public class ActivitiTest {

    @Test
    public void deploy() throws Exception {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("")
                .addClasspathResource("")
                .deploy();
        System.out.println(deployment.getId() + "   "+ deployment.getName());
    }

    @Test
    public void startProcess() throws Exception {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("");
        System.out.println("pid " + processInstance.getId() + ", activiti" + processInstance.getActivityId());

    }

    @Test
    public void queryTask() throws Exception {
        String assigne = "zhansan";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        List<org.activiti.engine.task.Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("").list();

        for (Task task : tasks) {
            System.out.println("taskId: " +task.getId() + "taskName" + task.getName());
            System.out.println("******************************");
        }

    }


    @Test
    public void completeTask() throws Exception {
        String taskId = "";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete(taskId);
        System.out.println("任务完成");
    }
}
