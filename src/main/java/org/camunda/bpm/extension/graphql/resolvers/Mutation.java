package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class Mutation implements GraphQLMutationResolver {

    private static final Logger LOGGER = Logger.getLogger(Mutation.class.getName());

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;


    public Mutation() {
    }

    public Task setAssignee(String taskEntityId, String assignee) {
        Task task = taskService.createTaskQuery().taskId(taskEntityId).singleResult();
        task.setAssignee(assignee);
        taskService.saveTask(task);
        return task;

    }

    public ProcessInstance createProcessInstance(String processDefintionKey, ArrayList<LinkedHashMap> variables) {
        return runtimeService.startProcessInstanceByKey(processDefintionKey, getVariablesMap(variables));
    }

    public Task claimTask(String taskId, String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.claim(taskId, userId);

        return taskService.createTaskQuery().initializeFormKeys().taskId(taskId).singleResult();
    }

    //@todo issue: ArrayList<LinkedHashMap> should be ArrayList<KeyValuePair>
    public ProcessInstance completeTask(String taskId, ArrayList<LinkedHashMap> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String piId = task.getProcessInstanceId();

        if (variables != null) {
            taskService.complete(taskId, getVariablesMap(variables));
        } else {
            taskService.complete(taskId);
        }


        if (piId != null) {
            return runtimeService.createProcessInstanceQuery().processInstanceId(piId).singleResult();
        } else {
            return null;
        }
    }

    public ProcessInstance startProcessInstanceByKey(String key) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(key);

        return runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();
    }

    public ProcessInstance startProcessInstanceByMessage(String message, String businesskey, ArrayList<LinkedHashMap> variables) {
        return runtimeService.startProcessInstanceByMessage(message, businesskey, getVariablesMap(variables));
    }

    private Map<String, Object> getVariablesMap (ArrayList<LinkedHashMap> variables) {
        Map<String, Object> map = new HashMap<>();
        for (LinkedHashMap i : variables) {
            switch (i.get("valueType").toString()) {
                case "STRING":      map.put(i.get("key").toString(), i.get("value")); break;
                case "INT":         map.put(i.get("key").toString(), Integer.parseInt(i.get("value").toString())); break;
                case "LONG":        map.put(i.get("key").toString(), Long.parseLong(i.get("value").toString())); break;
                case "FLOAT":       map.put(i.get("key").toString(), Float.parseFloat(i.get("value").toString())); break;
                case "DOUBLE":      map.put(i.get("key").toString(), Double.parseDouble(i.get("value").toString())); break;
                case "BOOLEAN":     map.put(i.get("key").toString(), Boolean.parseBoolean(i.get("value").toString())); break;
                default:            map.put(i.get("key").toString(), i.get("value")); break;
            }
        }
        return map;
    }

}

