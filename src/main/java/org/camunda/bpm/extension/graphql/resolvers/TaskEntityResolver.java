package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskEntityResolver implements GraphQLResolver<TaskEntity> {

    private ProcessEngine pe;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private IdentityService identityService;

    public TaskEntityResolver() {
        //super(TaskEntity.class);
        pe = BpmPlatform.getDefaultProcessEngine();
        if (pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        repositoryService = pe.getRepositoryService();
        runtimeService = pe.getRuntimeService();
        identityService = pe.getIdentityService();
    }

    public ProcessDefinition processDefinition(TaskEntity taskEntity) {
        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid != null) {
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(pdid);
            return processDefinition;
        } else
            return null;
    }

    public ProcessInstance processInstance (TaskEntity taskEntity) {
        String piId = taskEntity.getProcessInstanceId();
        if (piId != null) {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(piId).singleResult();
            return pi;
        } else
            return null;
    }

    public ExecutionEntity executionEntity(TaskEntity taskEntity) {
        //@todo: implement this!
        return null;
    }

    public User assignee(TaskEntity taskEntity) {
        String userId = taskEntity.getAssignee();

        if(userId != null) {
            User user = identityService.createUserQuery().userId(userId).singleResult();
            return user;
        } else {
            return null;
        }
    }

    public String contextPath(TaskEntity taskEntity) {
        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid != null) {
            String contextPath = ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(pe, taskEntity.getProcessDefinitionId());
            return contextPath;
        } else
            return null;
    }

    public List<HistoricVariableInstance> variables(TaskEntity taskEntity) {
        List<HistoricVariableInstance> variables = pe.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(taskEntity.getProcessInstanceId()).list();


        return variables;
    }
}
