package org.camunda.bpm.extension.graphql.history.process_instance;

import graphql.com.google.common.collect.Lists;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
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
}
