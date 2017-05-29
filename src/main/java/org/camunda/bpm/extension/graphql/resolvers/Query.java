package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
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
    private RepositoryService repositoryService;

    public Query() {
        super();
        pe = BpmPlatform.getDefaultProcessEngine();
        if(pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        taskService = pe.getTaskService();
        runtimeService = pe.getRuntimeService();
        repositoryService = pe.getRepositoryService();
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

    public List<ProcessDefinition> processDefinitions(Boolean isSuspended, Boolean latest) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery = (isSuspended != null && isSuspended == true) ? processDefinitionQuery.suspended() : processDefinitionQuery;
        processDefinitionQuery = (latest != null && latest == true) ? processDefinitionQuery.latestVersion() : processDefinitionQuery;

        List <ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
        return processDefinitionList;
    }
}
