package org.camunda.platform7.extension.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.platform7.extension.graphql.infratest.bpm.BaseTest;
import org.camunda.platform7.extension.graphql.infratest.bpm.services.WeatherCheckService;
import org.camunda.platform7.extension.graphql.infratest.comparators.Comparators;
import org.camunda.platform7.extension.graphql.infratest.comparators.DateExpressionMatcher;
import org.camunda.platform7.extension.graphql.infratest.comparators.IsANumberExpressionMatcher;
import org.camunda.platform7.extension.graphql.infratest.comparators.IsPresentExpressionMatcher;
import org.camunda.platform7.extension.graphql.infratest.scenarios.BDD;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Thread.sleep;
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

    private final CustomComparator comparators = Comparators.comparators(STRICT,
            IsANumberExpressionMatcher.isNumeric( "incidents[0].id"),
            DateExpressionMatcher.isDate( "incidents[0].incidentTimestamp"),
            IsANumberExpressionMatcher.isNumeric("incidents[0].executionId"),
            IsANumberExpressionMatcher.isNumeric("incidents[0].causeIncidentId"),
            IsANumberExpressionMatcher.isNumeric("incidents[0].rootCauseIncidentId"),
            IsANumberExpressionMatcher.isNumeric("incidents[0].jobDefinitionId"),
            IsANumberExpressionMatcher.isNumeric("incidents[0].processInstanceId"),
            IsPresentExpressionMatcher.isPresent("incidents[0].processDefinitionId"),
            IsANumberExpressionMatcher.isNumeric("incidents[0].configuration")
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
        BDD.Given("a query to search a incident by processId");
            String graphqlQuery = "query to find a incident by process id";
            String queryWithProcessId = query(graphqlQuery).replace("idIncidentProcess", processInstance.getId());
        BDD.When("graphql is called");
            sleep(3000);
            ExecutionResult executionResult = graphQL.execute(queryWithProcessId);
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        BDD.Then("the result should be");
            assertEquals(s("should return one incident"), result, comparators);
    }
}
