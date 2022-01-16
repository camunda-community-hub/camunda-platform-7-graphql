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

    private HistoryService historyService;

    public HistoricActivityInstanceResolver(HistoryService historyService) {
        this.historyService = historyService;
    }

    public List<HistoricActivityInstance> getHistoricActivityInstances(String processId, String activityType, String taskAssignee) {
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId) : query;
        query = !isBlank(activityType) ? query.activityType(activityType) : query;
        query = !isBlank(taskAssignee) ? query.taskAssignee(taskAssignee) : query;
        return query.list();
    }

}
