package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityResolver extends GraphQLResolver {

    private ProcessEngine pe;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;

    public TaskEntityResolver() {
        super(TaskEntity.class);
        pe = BpmPlatform.getDefaultProcessEngine();
        if (pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        repositoryService = pe.getRepositoryService();
        runtimeService = pe.getRuntimeService();
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
}
