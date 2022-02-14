package org.camunda.bpm.extension.graphql.history.activity_instance;


import graphql.kickstart.tools.GraphQLQueryResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Component
class HistoricActivityInstanceQuery implements GraphQLQueryResolver {

    private HistoryService historyService;

    public HistoricActivityInstanceQuery(HistoryService historyService) {
        this.historyService = historyService;
    }

    public List<HistoricActivityInstance> getHistoricActivityInstances(String processId, String activityType, String taskAssignee) {
        org.camunda.bpm.engine.history.HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId) : query;
        query = !isBlank(activityType) ? query.activityType(activityType) : query;
        query = !isBlank(taskAssignee) ? query.taskAssignee(taskAssignee) : query;
        return query.list();
    }



}
