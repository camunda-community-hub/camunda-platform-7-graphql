package org.camunda.bpm.extension.graphql;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import graphql.ExecutionResult;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.graphql.infratest.bpm.BaseTest;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CustomerData;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.InstanceVariables;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.Personality;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.SolvencyRating;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.CustomerDataService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processDefinition;
import static org.camunda.spin.Spin.JSON;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT;


public class GraphQLTest extends BaseTest {

    private static final String PROCESS_KEY = "credit-application";
    private static final String BUSINESS_KEY = "0000000072";
    private static final String EXAMPLE_ID = "01234567";
    private ProcessInstance processInstance;

    @Autowired
    private CustomerDataService customerDataServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        CustomerData customer = new CustomerData(EXAMPLE_ID, "Dummy Corp.", Personality.JURIDICAL, SolvencyRating.A);
        when(customerDataServiceMock.findById(EXAMPLE_ID)).thenReturn(customer);

        processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY, startFormEntries(450000));
        assertThat(processInstance).isNotEnded();
    }

    @After
    public void tearDown() throws Exception {
        runtimeService.deleteProcessInstance(processInstance.getId(), "JUnit test");
    }


    @Test
    public void processDeployed() throws Exception {
        ProcessDefinition definition = processDefinition(PROCESS_KEY);
        assertThat(definition.getName()).isEqualTo("Credit Application");
    }

    @Test
    public void queryProcessInstance() throws Exception {
        final String query = "{ processInstances { id } }";
        //expectedResult = "{\"processInstances\":[{\"id\":\"___an_Integer_____\"}]}";

        ExecutionResult executionResult = graphQL.execute(query);
        HashMap<String,Object> map = (HashMap<String,Object>)(executionResult.getData());

        assertThat(executionResult.getErrors().size()).as("Number of errors").isEqualTo(0);
        assertThat(map.size()).as("number of process instances").isEqualTo(1);
        assertThat(map.keySet().toArray()[0]).as("name of first key").isEqualTo("processInstances");

        String actualResult = JSON(map).toString();
        JsonObject obj = new JsonParser().parse(actualResult).getAsJsonObject();
        String idString = obj.get("processInstances").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
        Integer id = Integer.parseInt(idString);
        assertThat(id).as("processInstanceId").isGreaterThan(0);
    }

    @Test
    public void queryTasksName() throws Exception {
        final String query = "{ tasks { name } }";
        final String expectedResult = "{\"tasks\":[{\"name\":\"Find decision\"}]}";

        ExecutionResult executionResult = graphQL.execute(query);
        HashMap<String,Object> map = (HashMap<String,Object>)(executionResult.getData());
        String actualResult = JSON(map).toString();

        assertThat(executionResult.getErrors().size()).as("Number of errors").isEqualTo(0);
        assertThat(map.size()).as("number of tasks").isEqualTo(1);
        assertThat(actualResult).as("JSON result").isEqualTo(expectedResult);
    }

    @Test
    public void queryTasksVariables() throws Exception {
        final String query = "{ tasks { variables {key value valueType} } }";
        final String expectedResult = "{\"tasks\":[{\"variables\":[{\"key\":\"LOAN_PERIOD\",\"value\":\"24\",\"valueType\":\"LONG\"},{\"key\":\"DECISION\",\"value\":\"ESCALATE\",\"valueType\":\"STRING\"},{\"key\":\"CUSTOMER_ID\",\"value\":\"01234567\",\"valueType\":\"STRING\"},{\"key\":\"INTEREST_RATE\",\"value\":\"1.5\",\"valueType\":\"STRING\"},{\"key\":\"amountInEuro\",\"value\":\"450000\",\"valueType\":\"LONG\"},{\"key\":\"CREDIT_APPLICATION\",\"value\":\"{\\\"customerId\\\":\\\"01234567\\\",\\\"amountInEuro\\\":450000,\\\"interestRate\\\":1.5,\\\"loanPeriodInMonth\\\":24}\",\"valueType\":\"OBJECT\"},{\"key\":\"AMOUNT_IN_EURO\",\"value\":\"450000\",\"valueType\":\"LONG\"},{\"key\":\"CUSTOMER_DATA\",\"value\":\"{\\\"customerId\\\":\\\"01234567\\\",\\\"fullName\\\":\\\"Dummy Corp.\\\",\\\"personality\\\":\\\"JURIDICAL\\\",\\\"rating\\\":\\\"A\\\"}\",\"valueType\":\"OBJECT\"}]}]}";

        ExecutionResult executionResult = graphQL.execute(query);
        HashMap<String,Object> map = (HashMap<String,Object>)(executionResult.getData());
        String actualResult = JSON(map).toString();

        assertThat(executionResult.getErrors().size()).as("Number of errors").isEqualTo(0);
        assertThat(map.size()).as("number of variables").isEqualTo(1);
        assertEquals(actualResult, expectedResult, LENIENT);
    }

    @Test
    public void mutationClaimTask() throws Exception {
        final String queryId = "{ tasks { id }}";

        ExecutionResult executionResult = graphQL.execute(queryId);
        HashMap<String,Object> map = (HashMap<String,Object>)(executionResult.getData());
        String actualResult = JSON(map).toString();

        JsonObject obj = new JsonParser().parse(actualResult).getAsJsonObject();
        String id = obj.get("tasks").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();

        final String query = "mutation {\n" +
                "  claimTask(taskId: \"" + id + "\", userId:\"demo\") {\n" +
                "    id\n" +
                "    name\n" +
                "  }\n" +
                "}";

        final String expectedResult = "{\"claimTask\":{\"id\":\""+id+"\",\"name\":\"Find decision\"}}";
        executionResult = graphQL.execute(query);
        map = (HashMap<String,Object>)(executionResult.getData());
        actualResult = JSON(map).toString();

        assertThat(executionResult.getErrors().size()).as("Number of errors").isEqualTo(0);
        assertThat(actualResult).as("JSON result").isEqualTo(expectedResult);
    }



    private Map<String, Object> startFormEntries(long amount) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(InstanceVariables.CUSTOMER_ID, EXAMPLE_ID);
        variables.put(InstanceVariables.AMOUNT_IN_EURO, amount);
        variables.put(InstanceVariables.INTEREST_RATE, "1.5");
        variables.put(InstanceVariables.LOAN_PERIOD, 24L);
        return variables;
    }

}
