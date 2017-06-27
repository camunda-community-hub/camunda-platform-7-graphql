package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.extension.graphql.types.ValueTypeEnum;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mutation implements GraphQLRootResolver {

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

    public ProcessInstanceWithVariables createProcessInstance(String processDefintionKey, ArrayList<LinkedHashMap> variables) {
        ProcessInstantiationBuilder pib = runtimeService.createProcessInstanceByKey(processDefintionKey);

        if (variables != null) {

            pib.setVariables(getVariablesMap(variables));
            return pib.executeWithVariablesInReturn();
        } else {
            return pib.executeWithVariablesInReturn();
        }

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
            taskService.complete(taskId, getVariablesMap(variables));
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

    public ProcessInstance startProcessInstanceByKey(String key) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(key);

        return runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();
    }

    private Map<String, Object> getVariablesMap (ArrayList<LinkedHashMap> variables) {
        Map<String, Object> map = new HashMap<>();
        for (LinkedHashMap i : variables) {
            switch (i.get("valueType").toString()) {
                case "String":      map.put(i.get("key").toString(), i.get("value")); break;
                case "Int":         map.put(i.get("key").toString(), Long.parseLong(i.get("value").toString())); break;
                case "Float":       map.put(i.get("key").toString(), Double.parseDouble(i.get("value").toString())); break;
                case "Boolean":     map.put(i.get("key").toString(), Boolean.parseBoolean(i.get("value").toString())); break;
                default:            map.put(i.get("key").toString(), i.get("value")); break;
            }
        }
        return map;
    }

}
