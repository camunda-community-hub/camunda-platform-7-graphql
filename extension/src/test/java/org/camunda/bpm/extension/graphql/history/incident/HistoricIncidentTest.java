package org.camunda.bpm.extension.graphql.history.incident;

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

public class HistoricIncidentTest extends BaseTest {

    private static final String PROCESS_KEY = "weather-process";
    private static final String INCIDENT_BUSINESS_KEY = "9948848_incident";

    @Autowired
    private WeatherCheckService service;


    private final CustomComparator comparators = comparators(STRICT,
            isNumeric("historicIncidents[0].id"),
            isDate("historicIncidents[0].createTime"),
            isNumeric("historicIncidents[0].rootProcessInstanceId"),
            isNumeric("historicIncidents[0].processInstanceId"),
            isPresent("historicIncidents[0].processDefinitionId"),
            isNumeric("historicIncidents[0].causeIncidentId"),
            isNumeric("historicIncidents[0].rootCauseIncidentId"),
            isNumeric("historicIncidents[0].configuration"),
            isNumeric("historicIncidents[0].historyConfiguration"),
            isNumeric("historicIncidents[0].jobDefinitionId"),
            isNumeric("historicIncidents[0].executionId")
    );

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scenario.load("historic-incidents-scenarios.json");
        reset(this.service);
    }


    @Test
    public void shouldReturnTheHistoricIncidentByHProcessId() throws Exception {
        Given("a query to search a historic incident by process id");
            String graphqlQuery = "query to find historic incident by processId";
        And("the service return a exception");
            when(service.checkWeather()).thenThrow(new Exception("Exception to generate a incident"));
        And("The process starts");
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, INCIDENT_BUSINESS_KEY);
            sleep(2000);
        When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(query(graphqlQuery).replace("hProcessId", processInstance.getId()));
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
            assertEquals(s("should return one incident"), result, comparators);
    }
}
