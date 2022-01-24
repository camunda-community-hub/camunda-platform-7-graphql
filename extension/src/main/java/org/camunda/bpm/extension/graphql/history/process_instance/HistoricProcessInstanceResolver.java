package org.camunda.bpm.extension.graphql.history.process_instance;

import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoricProcessInstanceResolver implements GraphQLResolver<HistoricProcessInstance> {

    @Autowired
    HistoryService historyService;

    public String getId(HistoricProcessInstance instance) {
        return instance.getId();
    }

    public List<HistoricActivityInstance> getHistoricActivityInstances(HistoricProcessInstance processInstance) {
        final HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId());
        return query.list();
    }

    public List<HistoricIncident> getHistoricIncidents(HistoricProcessInstance processInstance) {
        HistoricIncidentQuery query = this.historyService.createHistoricIncidentQuery().processInstanceId(processInstance.getId());
        return query.list();
    }
}
