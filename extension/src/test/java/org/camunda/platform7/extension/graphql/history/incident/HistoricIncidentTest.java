package org.camunda.platform7.extension.graphql.history.incident;

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

public class HistoricIncidentTest extends BaseTest {

    private static final String PROCESS_KEY = "weather-process";
    private static final String INCIDENT_BUSINESS_KEY = "9948848_incident";

    @Autowired
    private WeatherCheckService service;


    private final CustomComparator comparators = Comparators.comparators(STRICT,
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].id"),
            DateExpressionMatcher.isDate("historicIncidents[0].createTime"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].rootProcessInstanceId"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].processInstanceId"),
            IsPresentExpressionMatcher.isPresent("historicIncidents[0].processDefinitionId"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].causeIncidentId"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].rootCauseIncidentId"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].configuration"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].historyConfiguration"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].jobDefinitionId"),
            IsANumberExpressionMatcher.isNumeric("historicIncidents[0].executionId")
    );

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scenario.load("historic-incidents-scenarios.json");
        reset(this.service);
    }


    @Test
    public void shouldReturnTheHistoricIncidentByHProcessId() throws Exception {
        BDD.Given("a query to search a historic incident by process id");
            String graphqlQuery = "query to find historic incident by processId";
        BDD.And("the service return a exception");
            when(service.checkWeather()).thenThrow(new Exception("Exception to generate a incident"));
        BDD.And("The process starts");
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, INCIDENT_BUSINESS_KEY);
            sleep(2000);
        BDD.When("graphql is called");
            ExecutionResult executionResult = graphQL.execute(query(graphqlQuery).replace("hProcessId", processInstance.getId()));
            String result = new ObjectMapper().writeValueAsString(executionResult.getData());
        BDD.Then("the result should be");
            assertEquals(s("should return one incident"), result, comparators);
    }
}
