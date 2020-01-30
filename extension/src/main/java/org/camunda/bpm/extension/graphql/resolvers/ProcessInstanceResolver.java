package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProcessInstanceResolver implements GraphQLResolver<ProcessInstance> {

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

    public ProcessInstanceResolver() {
    }

    public String getId(ProcessInstance processInstance) {
        return processInstance.getId();
    }

    public String getProcessInstanceId(ProcessInstance processInstance) {
        return processInstance.getProcessInstanceId();
    }

    public String getTenantId(ProcessInstance processInstance) {
        return processInstance.getTenantId();
    }

    public Boolean isEnded(ProcessInstance processInstance) {
        return processInstance.isEnded();
    }

    public List<KeyValuePair> variables(ProcessInstance processInstance) {
        List<KeyValuePair> keyValuePairs;

        String pdid = processInstance.getProcessDefinitionId();
        if (pdid == null)
            return null;

        try {
            Util.switchContext(repositoryService, pdid, processEngineConfiguration);
            VariableMap variableMap = runtimeService.getVariablesTyped(processInstance.getId());
            keyValuePairs = Util.getKeyValuePairs(variableMap);

        } finally {
            ProcessApplicationContext.clear();
        }
        return keyValuePairs;
    }
}
