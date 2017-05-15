package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLRootResolver {

    private ProcessEngine pe;

    private TaskService taskService;
    private RuntimeService runtimeService;

    public Query() {
        super();
        pe = BpmPlatform.getDefaultProcessEngine();
        if(pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        taskService = pe.getTaskService();
        runtimeService = pe.getRuntimeService();
    }

    public List<Task> tasks(String assignee, String name, String nameLike) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = (assignee != null) ? taskQuery.taskAssignee(assignee):taskQuery;
        taskQuery = (name != null) ? taskQuery.taskName(name):taskQuery;
        taskQuery = (nameLike != null) ? taskQuery.taskNameLike(nameLike):taskQuery;
        taskQuery.initializeFormKeys();
        List<Task> tasks = taskQuery.list();
        return tasks;
    }

    public Task task(String id) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = taskQuery.taskId(id);
        taskQuery.initializeFormKeys();
        Task task = taskQuery.singleResult();
        return task;
    }

    public List<ProcessInstance> processesInstances(String businessKey) {
    	ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
    	processInstanceQuery = (businessKey != null) ? processInstanceQuery.processInstanceBusinessKey(businessKey):processInstanceQuery;
        List<ProcessInstance> processInstances = processInstanceQuery.list();
        return processInstances;
    }
 
    public ProcessInstance processInstance(String id, String businessKey){
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		processInstanceQuery = (businessKey != null) ? processInstanceQuery.processInstanceBusinessKey(businessKey):processInstanceQuery;
		ProcessInstance processInstance = processInstanceQuery.processInstanceId(id).singleResult();
		return processInstance;
    }    
}
