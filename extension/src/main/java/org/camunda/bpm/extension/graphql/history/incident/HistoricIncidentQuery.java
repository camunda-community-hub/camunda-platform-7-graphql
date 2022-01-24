package org.camunda.bpm.extension.graphql.history.incident;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricIncident;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoricIncidentQuery implements GraphQLQueryResolver {

    private HistoryService historyService;

    public HistoricIncidentQuery(HistoryService historyService) {
        this.historyService = historyService;
    }


    public List<HistoricIncident> getHistoricIncidents(String aProcInstId) {
        org.camunda.bpm.engine.history.HistoricIncidentQuery query = historyService.createHistoricIncidentQuery().processInstanceId(aProcInstId);
        return query.list();
    }

}
