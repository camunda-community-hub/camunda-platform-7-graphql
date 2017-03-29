package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Mutation extends GraphQLRootResolver {

    private ProcessEngine pe;
    private TaskService taskService;
    private RuntimeService runtimeService;

    public Mutation() {
        super();
        pe = BpmPlatform.getDefaultProcessEngine();
        if(pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        taskService = pe.getTaskService();
        runtimeService = pe.getRuntimeService();

    }

    public Task setAssignee(String taskEntityId, String assignee) {
        Task task = taskService.createTaskQuery().taskId(taskEntityId).singleResult();
        task.setAssignee(assignee);
        taskService.saveTask(task);
        return task;

    }

    public ProcessInstance createProcessInstance(String processDefintionKey) {
        ProcessInstance pi = runtimeService.createProcessInstanceByKey(processDefintionKey).executeWithVariablesInReturn();
        return pi;
    }

    public ProcessInstance claimTask(String taskId, String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.claim(taskId, userId);

        String piId = task.getProcessInstanceId();
        if (piId != null) {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(piId).singleResult();
            return pi;
        } else {
            return null;
        }
    }

    //@todo: implement next
    public ProcessInstance completeTask(String taskId, Map<String, String> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().caseInstanceId(task.getCaseInstanceId()).singleResult();

        return pi;
    }
}
