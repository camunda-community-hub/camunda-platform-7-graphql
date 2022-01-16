package org.camunda.bpm.extension.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.graphql.infratest.bpm.BaseTest;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.WeatherCheckService;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Thread.sleep;
import static org.camunda.bpm.extension.graphql.infratest.comparators.Comparators.comparators;
import static org.camunda.bpm.extension.graphql.infratest.comparators.DateExpressionMatcher.isDate;
import static org.camunda.bpm.extension.graphql.infratest.comparators.IsANumberExpressionMatcher.isNumeric;
import static org.camunda.bpm.extension.graphql.infratest.comparators.IsPresentExpressionMatcher.isPresent;
import static org.camunda.bpm.extension.graphql.infratest.scenarios.BDD.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class IncidentTest extends BaseTest {

    private static final String PROCESS_KEY = "weather-process";
    private static final String BUSINESS_KEY = "985569948";

    @Autowired
    private WeatherCheckService service;
    private ProcessInstance processInstance;

    private final CustomComparator comparators = comparators(STRICT,
            isNumeric( "incidents[0].id"),
            isDate( "incidents[0].incidentTimestamp"),
            isNumeric("incidents[0].executionId"),
            isNumeric("incidents[0].causeIncidentId"),
            isNumeric("incidents[0].rootCauseIncidentId"),
            isNumeric("incidents[0].jobDefinitionId"),
            isNumeric("incidents[0].processInstanceId"),
            isPresent("incidents[0].processDefinitionId"),
            isNumeric("incidents[0].configuration")
    );

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scenario.load("incident-scenarios.json");
        reset(this.service);
        when(service.checkWeather()).thenThrow(new Exception("Exception to generate a incident"));
        processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY);
    }

    @Test
    public void shouldReturnTheIncidentWhenDelegateThrowAException() throws Exception {
        Given("a query to search a incident by processId");
            String graphqlQuery = "query to find a incident by process id";
            String queryWithProcessId = query(graphqlQuery).replace("idIncidentProcess", processInstance.getId());
        When("graphql is called");
            sleep(3000);
            ExecutionResult executionResult = graphQL.execute(queryWithProcessId);
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
            assertEquals(s("should return one incident"), result, comparators);
    }
}
