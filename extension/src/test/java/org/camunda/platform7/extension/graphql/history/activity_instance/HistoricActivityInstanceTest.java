package org.camunda.platform7.extension.graphql.history.activity_instance;

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
import org.junit.After;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Thread.sleep;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
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

    private final CustomComparator comparators = Comparators.comparators(STRICT, IsANumberExpressionMatcher.isNumeric("id"),
            IsANumberExpressionMatcher.isNumeric("historicActivityInstances[0,5].executionId"),
            IsPresentExpressionMatcher.isPresent("historicActivityInstances[0,5].id"),
            IsPresentExpressionMatcher.isPresent("historicActivityInstances[4].taskId"),
            IsANumberExpressionMatcher.isNumeric("historicActivityInstances[0,5].durationInMillis"),
            IsANumberExpressionMatcher.isNumeric("historicActivityInstances[0,5].parentActivityInstanceId"),
            IsANumberExpressionMatcher.isNumeric("historicActivityInstances[0,5].processInstanceId"),
            IsPresentExpressionMatcher.isPresent("historicActivityInstances[0,5].processDefinitionId"),
            IsANumberExpressionMatcher.isNumeric("historicActivityInstances[0,5].rootProcessInstanceId"),
            DateExpressionMatcher.isDate("historicActivityInstances[0,5].startTime"),
            DateExpressionMatcher.isDate("historicActivityInstances[0,5].endTime")

    );

    private final CustomComparator byFilterComparators = Comparators.comparators(STRICT,
            IsPresentExpressionMatcher.isPresent("historicActivityInstances[0].id")
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
        BDD.Given("a query to search a historic activity instance by processId");
            String graphqlQuery = "query to find activity instances by processId";
        BDD.When("graphql is called");
            String queryWithProcessId = query(graphqlQuery).replace("idActivityProcess", processInstance.getId());
        ExecutionResult executionResult = graphQL.execute(queryWithProcessId);
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        BDD.Then("the result should be");
            assertEquals(s("result of historic activity instances"), result, comparators);
    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceVariablesUsingFilter() throws Exception {
        BDD.Given("a query to search a historic activity instance filter temperature variable");
            String graphqlQuery = "query to find activity instances with variables filtered";
        BDD.When("graphql is called");
            String queryWithProcessId = query(graphqlQuery).replace("idActivityProcess", processInstance.getId());
            ExecutionResult executionResult = graphQL.execute(queryWithProcessId);
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        BDD.Then("the result should be");
            assertEquals(s("result of historic activity instances variables filtered"), result, comparators);
    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceByActivityType() throws Exception {
        BDD.Given("a query to search a historic activity instance by activityType");
            String graphqlQuery = "query to find activity instances by activityType";
        BDD.When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        BDD.Then("the result should be");
            assertEquals(s("should return one historic activity by activityType"), result, byFilterComparators);
    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceByTaskAssignee() throws Exception {
        BDD.Given("a query to search a historic activity instance by task assignee");
            String graphqlQuery = "query to find activity instances by task assignee";
        BDD.When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(query(graphqlQuery));
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        BDD.Then("the result should be");
            assertEquals(s("should return one historic activity by task assignee"), result, byFilterComparators);
    }
}
