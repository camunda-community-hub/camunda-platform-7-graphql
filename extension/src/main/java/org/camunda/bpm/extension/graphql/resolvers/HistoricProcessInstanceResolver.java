package org.camunda.bpm.extension.graphql.resolvers;


import graphql.kickstart.tools.GraphQLQueryResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Component
class HistoricProcessInstanceResolver implements GraphQLQueryResolver {

    @Autowired
    HistoryService historyService;

    public HistoricProcessInstanceResolver() {
    }

    public List<HistoricProcessInstance> getHistoricProcessInstances(String processId, String businessKey) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId): query;
        query = !isBlank(businessKey) ? query.processInstanceBusinessKey(businessKey): query;
        return query.list();
    }

}
