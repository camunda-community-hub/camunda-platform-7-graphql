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
import java.util.ArrayList;
import java.util.List;


@Component
public class ExecutionEntityResolver implements GraphQLResolver<ExecutionEntity> {

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

    public ExecutionEntityResolver() {
    }

    public List<KeyValuePair> variables(ProcessInstance processInstance) {

        List<KeyValuePair> keyValuePairs;

        try {
            String pdid = processInstance.getProcessDefinitionId();
            if (pdid == null) {
                return null;
            }

            Util.switchContext(repositoryService, pdid, processEngineConfiguration);

            VariableMap variableMap = runtimeService.getVariablesTyped(processInstance.getId());

            keyValuePairs = Util.getKeyValuePairs(variableMap);

        } finally {
            ProcessApplicationContext.clear();
        }
        return keyValuePairs;
    }
}
