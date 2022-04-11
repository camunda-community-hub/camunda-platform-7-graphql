package org.camunda.platform7.extension.graphql.history.activity_instance;


import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.platform7.extension.graphql.resolvers.Util;
import org.camunda.platform7.extension.graphql.types.KeyValuePair;
import org.camunda.platform7.extension.graphql.types.filters.keyvalue.KeyValueFilter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
class HistoricActivityInstanceResolver implements GraphQLResolver<HistoricActivityInstance> {

    private HistoryService historyService;

    public HistoricActivityInstanceResolver(HistoryService historyService) {
        this.historyService = historyService;
    }


    public List<KeyValuePair> getVariables(HistoricActivityInstance instance, KeyValueFilter filter) {
        List<String> keysTreated = filter == null ? Arrays.asList() : filter.getKey().getIn();

        HistoricVariableInstanceQuery query = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getProcessInstanceId());
        VariableMap variableMap = new VariableMapImpl();
        query.list().stream().filter(item -> keysTreated.isEmpty() || keysTreated.contains(item.getName()))
                .forEach(item -> variableMap.putValueTyped(item.getName(), item.getTypedValue()));

        return Util.getKeyValuePairs(variableMap);
    }

}
