package org.camunda.bpm.extension.graphql.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.runtime.IncidentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class IncidentResolver implements GraphQLQueryResolver {

    @Autowired
    RuntimeService runtimeService;

    public List<Incident> getIncidents(String processId) {
        IncidentQuery query = runtimeService.createIncidentQuery();
        query = !isBlank(processId) ? query.processInstanceId(processId): query;
        return query.list();
    }

}
