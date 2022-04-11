package org.camunda.platform7.extension.graphql.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Incident;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class IncidentQuery implements GraphQLQueryResolver {

    private RuntimeService runtimeService;

    public IncidentQuery(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public List<Incident> getIncidents(String processId) {
        org.camunda.bpm.engine.runtime.IncidentQuery query = runtimeService.createIncidentQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId): query;
        return query.list();
    }

}
