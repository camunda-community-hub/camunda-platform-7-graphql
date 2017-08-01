package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.ObjectValueImpl;
import org.camunda.bpm.engine.variable.type.SerializableValueType;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.camunda.spin.Spin.JSON;

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
        if (pdid != null) {
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(pdid);
            return processDefinition;
        } else
            return null;
    }

    public ProcessInstance processInstance(TaskEntity taskEntity) {
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

        if (userId != null) {
            User user = identityService.createUserQuery().userId(userId).singleResult();
            return user;
        } else {
            return null;
        }
    }

    public String contextPath(TaskEntity taskEntity) {
        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid != null) {
            String contextPath = ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(processEngine, taskEntity.getProcessDefinitionId());
            return contextPath;
        } else
            return null;
    }

    public List<KeyValuePair> variables(TaskEntity taskEntity) {
        ArrayList<KeyValuePair> keyValuePairs = new ArrayList<>();

        try {
            String pdid = taskEntity.getProcessDefinitionId();
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(pdid);
            String deploymentId = processDefinition.getDeploymentId();
            ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();
            ProcessApplicationReference targetProcessApplication = processApplicationManager.getProcessApplicationForDeployment(deploymentId);

            if (targetProcessApplication != null) {
                String processApplicationName = targetProcessApplication.getName();
                ProcessApplicationContext.setCurrentProcessApplication(processApplicationName);
            }
            VariableMap variableMap = taskService.getVariablesTyped(taskEntity.getId(), true);

            for (VariableMap.Entry<String, Object> i : variableMap.entrySet()) {

                Object objValue = i.getValue();
                String key = i.getKey();
                TypedValue typedValue = variableMap.getValueTyped(key);

                if (objValue != null) {
                    String value;
                    if (typedValue.getType() == SerializableValueType.OBJECT) {

                        ObjectValueImpl objectValueImpl = ((ObjectValueImpl) typedValue);
                        if (objectValueImpl.getSerializationDataFormat().equals(Variables.SerializationDataFormats.JSON.toString())) {
                            value = JSON(key).toString();
                        } else {
                            value = typedValue.getValue().toString();
                        }

                    } else {
                        value = objValue.toString();
                    }

                    KeyValuePair keyValuePair = new KeyValuePair(key, value, variableMap.getValueTyped(key).getType().toString());
                    keyValuePairs.add(keyValuePair);
                } else {
                    System.out.println("objValue is null: " + key);
                }
            }
        } finally {
            ProcessApplicationContext.clear();
        }


        return keyValuePairs;
    }
}
