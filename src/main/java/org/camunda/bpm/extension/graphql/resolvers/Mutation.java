package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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

    public Task claimTask(String taskId, String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.claim(taskId, userId);

        return task;
    }

    //@todo issue: ArrayList<LinkedHashMap> should be ArrayList<KeyValuePair>
    public ProcessInstance completeTask(String taskId, ArrayList<LinkedHashMap> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String piId = task.getProcessInstanceId();

        if (variables != null) {
            Map<String, Object> map = new HashMap<>();
            for (LinkedHashMap i : variables) map.put(i.get("key").toString(), i.get("value"));

            // for (KeyValuePair i: variables) map.put(i.getKey(), i.getValue());

            taskService.complete(taskId, map);
        } else {
            taskService.complete(taskId);
        }


        if (piId != null) {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(piId).singleResult();
            return pi;
        } else {
            return null;
        }



    }
}
