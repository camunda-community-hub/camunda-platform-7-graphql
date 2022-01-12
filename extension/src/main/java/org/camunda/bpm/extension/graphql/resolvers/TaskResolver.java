package org.camunda.bpm.extension.graphql.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;


@Component
public class TaskResolver implements GraphQLResolver<Task> {

    private final static Logger LOGGER = Logger.getLogger(TaskResolver.class.getName());

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    IdentityService identityService;

    public TaskResolver() {
    }

    public ProcessDefinition processDefinition(Task task) {
        String pdid = task.getProcessDefinitionId();
        if (pdid == null)
            return null;

        return repositoryService.getProcessDefinition(pdid);

    }

    public ProcessInstance processInstance(Task task) {
        String piId = task.getProcessInstanceId();
        if (piId == null)
            return null;

        return runtimeService.createProcessInstanceQuery().processInstanceId(piId).singleResult();
    }

    public Execution executionEntity(Task task) {
        String executionId = task.getExecutionId();
        if (executionId == null)
            return null;

        return runtimeService.createExecutionQuery()
                .executionId(executionId)
                .singleResult();
    }

    public User assignee(Task task) {
        String userId = task.getAssignee();

        if (userId == null)
            return null;

        return identityService.createUserQuery().userId(userId).singleResult();

    }

    public String contextPath(Task task) {
        String pdid = task.getProcessDefinitionId();
        if (pdid == null)
            return null;

        return ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(processEngine, task.getProcessDefinitionId());

    }

    public List<KeyValuePair> variables(Task task) {
        List<KeyValuePair> keyValuePairs;

        String pdid = task.getProcessDefinitionId();
        if (pdid == null)
            return null;

        try {
            Util.switchContext(repositoryService, pdid, processEngineConfiguration);
            VariableMap variableMap = taskService.getVariablesTyped(task.getId(), true);
            keyValuePairs = Util.getKeyValuePairs(variableMap);

        } finally {
            ProcessApplicationContext.clear();
        }


        return keyValuePairs;
    }
}
