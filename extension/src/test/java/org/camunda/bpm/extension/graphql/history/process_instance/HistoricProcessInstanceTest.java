package org.camunda.bpm.extension.graphql.history.process_instance;

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

public class HistoricProcessInstanceTest extends BaseTest {

    private static final String PROCESS_KEY = "weather-process";
    private static final String BUSINESS_KEY = "552365";
    private static final String BUSINESS_KEY_WITH_ACTIVITIES = "552365_activities";
    private static final String INCIDENT_BUSINESS_KEY = "552365_incident";

    @Autowired
    private WeatherCheckService service;
    private ProcessInstance processInstance;

    private final CustomComparator comparatorsWithActivities = comparators(STRICT,
            isNumeric("historicProcessInstances[0].id"),
            isNumeric("historicProcessInstances[0].historicActivityInstances[0,5].parentActivityInstanceId"),
            isPresent("historicProcessInstances[0].historicActivityInstances[0,5].id"),
            isPresent("historicProcessInstances[0].historicActivityInstances[0,5].id")
    );

    private final CustomComparator comparatorsWithIncidents = comparators(STRICT,
            isNumeric("historicProcessInstances[0].id"),
            isNumeric("historicProcessInstances[0].historicIncidents[0].id"),
            isNumeric("historicProcessInstances[0].historicIncidents[0].processInstanceId")
    );

    private final CustomComparator comparators = comparators(STRICT,
            isNumeric("historicProcessInstances[0].id"),
            isNumeric("historicProcessInstances[0].durationInMillis"),
            isNumeric("historicProcessInstances[0].rootProcessInstanceId"),
            isDate("historicProcessInstances[0].startTime"),
            isDate("historicProcessInstances[0].endTime")
    );

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scenario.load("historic-process-instance-scenarios.json", "incident-scenarios.json");
        reset(this.service);
    }

    @Test
    public void shouldReturnTheHistoricProcessInstance() throws Exception {
        Given("a query to search a historic process instance by businessKey");
        String graphqlQuery = "query to find process instance by businessKey";
        And("the process is created");
        when(service.checkWeather()).thenReturn(30);
        runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY);
        sleep(1000);
        When("graphql is called");
        ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
        String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
        assertEquals(s("result of historic process instances"), result, comparators);
    }

    @Test
    public void shouldReturnTheHistoricProcessInstanceWithHistoricActivityInstance() throws Exception {
        Given("a query to search a historic process instance by businessKey");
        String graphqlQuery = "query to find process instance with activity instances associated";
        And("the process is created");
        when(service.checkWeather()).thenReturn(40);
        runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY_WITH_ACTIVITIES);
        sleep(1000);
        When("graphql is called");
        ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
        String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
        assertEquals(s("result should be 1 process instance with 4 activity instances"), result, comparatorsWithActivities);
    }

    @Test
    public void shouldReturnTheHistoricProcessInstanceWithHistoricIncidents() throws Exception {
        Given("a query to search a historic process instance by businessKey");
        String graphqlQuery = "query to find process instance with incidents associated";
        And("the service return a exception");
        when(service.checkWeather()).thenThrow(new Exception("Exception to generate a incident"));
        And("The process starts");
        runtimeService.startProcessInstanceByKey(PROCESS_KEY, INCIDENT_BUSINESS_KEY);
        sleep(2000);
        When("graphql is called");
        ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
        String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
        assertEquals(s("result should be 1 process instance with 1 historic incident"), result, comparatorsWithIncidents);
    }
}
