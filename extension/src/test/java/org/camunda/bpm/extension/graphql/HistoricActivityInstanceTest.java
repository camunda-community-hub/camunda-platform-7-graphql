package org.camunda.bpm.extension.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.graphql.infratest.bpm.BaseTest;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.WeatherCheckService;
import org.camunda.bpm.extension.graphql.infratest.comparators.IsPresentExpressionMatcher;
import org.camunda.bpm.extension.graphql.infratest.comparators.StringSizeExpressionMatcher;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.camunda.bpm.extension.graphql.infratest.comparators.Comparators.comparators;
import static org.camunda.bpm.extension.graphql.infratest.comparators.DateExpressionMatcher.isDate;
import static org.camunda.bpm.extension.graphql.infratest.comparators.IsANumberExpressionMatcher.isNumeric;
import static org.camunda.bpm.extension.graphql.infratest.comparators.IsPresentExpressionMatcher.isPresent;
import static org.camunda.bpm.extension.graphql.infratest.comparators.StringSizeExpressionMatcher.stringSize;
import static org.camunda.bpm.extension.graphql.infratest.scenarios.BDD.*;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class HistoricActivityInstanceTest extends BaseTest {

    private static final String PROCESS_KEY = "weather-process";
    private static final String BUSINESS_KEY = "9985465";

    @Autowired
    private WeatherCheckService service;
    private ProcessInstance processInstance;
    private final CustomComparator comparators = comparators(STRICT, isNumeric( "id"),
            isNumeric("historicActivityInstances[0,4].executionId"),
            isPresent("historicActivityInstances[0,4].id"),
            isNumeric("historicActivityInstances[0,4].durationInMillis"),
            isNumeric("historicActivityInstances[0,4].parentActivityInstanceId"),
            isNumeric("historicActivityInstances[0,4].processInstanceId"),
            isNumeric("historicActivityInstances[0,4].rootProcessInstanceId"),
            isDate("historicActivityInstances[0,4].startTime"),
            isDate("historicActivityInstances[0,4].endTime")

    );

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scenario.load("historic-activity-instance-scenarios.json");
        when(service.checkWeather()).thenReturn(24);
        processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY);

    }

    @Test
    public void shouldReturnTheHistoricActivityInstanceByProcessId() throws JSONException, JsonProcessingException {
        Given("a query to search a historic activity instance by processId");
            String graphqlQuery = "query to find activity instances by processId";
            String queryWithProcessId = query(graphqlQuery).replace("idActivityProcess", processInstance.getId());
        When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(queryWithProcessId);
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        Then("the result should be");
            assertEquals(s("result of historic activity instances"), result, comparators);
    }
}
