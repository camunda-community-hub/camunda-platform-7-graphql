package org.camunda.bpm.extension.graphql.history.activity_instance;


import graphql.kickstart.tools.GraphQLQueryResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Component
class HistoricActivityInstanceResolver implements GraphQLQueryResolver {

    @Autowired
    HistoryService historyService;

    public List<HistoricActivityInstance> getHistoricActivityInstances(String processId) {
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId) : query;
        return query.list();
    }

}
