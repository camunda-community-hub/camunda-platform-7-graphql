package org.camunda.bpm.extension.graphql.history.process_instance;


import graphql.kickstart.tools.GraphQLQueryResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Component
class HistoricProcessInstanceQuery implements GraphQLQueryResolver {

    HistoryService historyService;

    public HistoricProcessInstanceQuery(HistoryService historyService) {
        this.historyService = historyService;
    }

    public List<HistoricProcessInstance> getHistoricProcessInstances(String processId, String businessKey) {
        org.camunda.bpm.engine.history.HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId) : query;
        query = !isBlank(businessKey) ? query.processInstanceBusinessKey(businessKey) : query;
        return query.list();
    }


}
