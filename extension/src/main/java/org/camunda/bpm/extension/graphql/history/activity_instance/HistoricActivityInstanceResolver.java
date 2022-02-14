package org.camunda.bpm.extension.graphql.history.activity_instance;


import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.bpm.extension.graphql.resolvers.Util;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
class HistoricActivityInstanceResolver implements GraphQLResolver<HistoricActivityInstance> {

    private HistoryService historyService;

    public HistoricActivityInstanceResolver(HistoryService historyService) {
        this.historyService = historyService;
    }


    public List<KeyValuePair> getVariables(HistoricActivityInstance instance) {
        HistoricVariableInstanceQuery query = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getProcessInstanceId());
        VariableMap variableMap = new VariableMapImpl();
        query.list().forEach(item -> variableMap.putValueTyped(item.getName(), item.getTypedValue()));

        return Util.getKeyValuePairs(variableMap);
    }

}
