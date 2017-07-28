package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
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
public class ProcessDefinitionEntityResolver implements GraphQLResolver<ProcessDefinitionEntity> {

    @Autowired
    ProcessEngine pe;

    @Autowired
    RepositoryService repositoryService;


    public ProcessDefinitionEntityResolver() {
    }

    public String startFormKey(ProcessDefinition processDefinition) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinition.getId());
        Collection<StartEvent> startEvents = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        StartEvent startEvent = startEvents.iterator().next();
        String formKey = startEvent.getCamundaFormKey();
        return formKey;
    }

    public String contextPath(ProcessDefinition processDefinition) {
        String pdid = processDefinition.getId();
        if (pdid != null) {
            String contextPath = ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(pe, pdid);
            return contextPath;
        } else
            return null;
    }
}
