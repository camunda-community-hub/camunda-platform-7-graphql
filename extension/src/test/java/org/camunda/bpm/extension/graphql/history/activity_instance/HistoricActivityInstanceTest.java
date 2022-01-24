package org.camunda.bpm.extension.graphql.history.activity_instance;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.graphql.infratest.bpm.BaseTest;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.WeatherCheckService;
import org.junit.After;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Thread.sleep;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.camunda.bpm.extension.graphql.infratest.comparators.Comparators.comparators;
import static org.camunda.bpm.extension.graphql.infratest.comparators.DateExpressionMatcher.isDate;
import static org.camunda.bpm.extension.graphql.infratest.comparators.IsANumberExpressionMatcher.isNumeric;
import static org.camunda.bpm.extension.graphql.infratest.comparators.IsPresentExpressionMatcher.isPresent;
import static org.camunda.bpm.extension.graphql.infratest.scenarios.BDD.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class HistoricActivityInstanceTest extends BaseTest {

    private static final String PROCESS_KEY2 = "weather-process2";
    private static final String BUSINESS_KEY = "9985465";


    @Autowired
    private WeatherCheckService service;
    private ProcessInstance processInstance;

    private final CustomComparator comparators = comparators(STRICT, isNumeric("id"),
            isNumeric("historicActivityInstances[0,5].executionId"),
            isPresent("historicActivityInstances[0,5].id"),
            isPresent("historicActivityInstances[4].taskId"),
            isNumeric("historicActivityInstances[0,5].durationInMillis"),
            isNumeric("historicActivityInstances[0,5].parentActivityInstanceId"),
            isNumeric("historicActivityInstances[0,5].processInstanceId"),
            isPresent("historicActivityInstances[0,5].processDefinitionId"),
            isNumeric("historicActivityInstances[0,5].rootProcessInstanceId"),
            isDate("historicActivityInstances[0,5].startTime"),
            isDate("historicActivityInstances[0,5].endTime")

    );

    private final CustomComparator byFilterComparators = comparators(STRICT,
            isPresent("historicActivityInstances[0].id")
    );
    private static Boolean runOneTimeBeforeAll = false;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scenario.load("historic-activity-instance-scenarios.json");
        reset(service);
        when(service.checkWeather()).thenReturn(26);
        processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY2, BUSINESS_KEY);
        sleep(2000);
        assertThat(processInstance).isWaitingAt("confirmGoToTheBeach");
        complete(task());
    }

    @After
    public void tearDown() {
        historyService.deleteHistoricProcessInstance(processInstance.getId());
    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceByProcessId() throws Exception {
        Given("a query to search a historic activity instance by processId");
            String graphqlQuery = "query to find activity instances by processId";
        When("graphql is called");
            String queryWithProcessId = query(graphqlQuery).replace("idActivityProcess", processInstance.getId());
        ExecutionResult executionResult = graphQL.execute(queryWithProcessId);
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
            assertEquals(s("result of historic activity instances"), result, comparators);
    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceByActivityType() throws Exception {
        Given("a query to search a historic activity instance by activityType");
            String graphqlQuery = "query to find activity instances by activityType";
        When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
            assertEquals(s("should return one historic activity by activityType"), result, byFilterComparators);
    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceByTaskAssignee() throws Exception {
        Given("a query to search a historic activity instance by task assignee");
            String graphqlQuery = "query to find activity instances by task assignee";
        When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
            assertEquals(s("should return one historic activity by task assignee"), result, byFilterComparators);
    }
}
