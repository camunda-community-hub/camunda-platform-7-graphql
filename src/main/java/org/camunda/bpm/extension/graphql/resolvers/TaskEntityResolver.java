package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class TaskEntityResolver implements GraphQLResolver<TaskEntity> {

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

    public TaskEntityResolver() {
    }

    public ProcessDefinition processDefinition(TaskEntity taskEntity) {
        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid == null)
            return null;

        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(pdid);
        return processDefinition;

    }

    public ProcessInstance processInstance(TaskEntity taskEntity) {
        String piId = taskEntity.getProcessInstanceId();
        if (piId == null)
            return null;

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(piId).singleResult();
        return pi;
    }

    public ExecutionEntity executionEntity(TaskEntity taskEntity) {
        //@todo: implement this!
        return null;
    }

    public User assignee(TaskEntity taskEntity) {
        String userId = taskEntity.getAssignee();

        if (userId == null)
            return null;

        User user = identityService.createUserQuery().userId(userId).singleResult();
        return user;

    }

    public String contextPath(TaskEntity taskEntity) {
        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid == null)
            return null;

        String contextPath = ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(processEngine, taskEntity.getProcessDefinitionId());
            return contextPath;

    }

    public List<KeyValuePair> variables(TaskEntity taskEntity) {
        List<KeyValuePair> keyValuePairs;

        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid == null)
            return null;

        try {
            Util.switchContext(repositoryService, pdid, processEngineConfiguration);
            VariableMap variableMap = taskService.getVariablesTyped(taskEntity.getId(), true);
            keyValuePairs = Util.getKeyValuePairs(variableMap);

        } finally {
            ProcessApplicationContext.clear();
        }


        return keyValuePairs;
    }
}
