package org.camunda.platform7.extension.graphql.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by danielvogel on 20.06.17.
 */

@Component
public class ProcessDefinitionResolver implements GraphQLResolver<ProcessDefinition> {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RepositoryService repositoryService;


    public ProcessDefinitionResolver() {
    }

    public String getId(ProcessDefinition processDefinition) {
        return processDefinition.getId();
    }

    public String getName(ProcessDefinition processDefinition) {
        return processDefinition.getName();
    }

    public String getKey(ProcessDefinition processDefinition) {
        return processDefinition.getKey();
    }

    public String startFormKey(ProcessDefinition processDefinition) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinition.getId());
        Collection<StartEvent> startEvents = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        StartEvent startEvent = startEvents.iterator().next();
        return startEvent.getCamundaFormKey();
    }

    public String contextPath(ProcessDefinition processDefinition) {
        String pdid = processDefinition.getId();
        if (pdid != null) {
            return ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(processEngine, pdid);
        } else
            return null;
    }
}
