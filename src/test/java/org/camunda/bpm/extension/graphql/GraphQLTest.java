package org.camunda.bpm.extension.graphql;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.ExecutionStrategy;
import graphql.schema.GraphQLSchema;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import static org.camunda.spin.Spin.JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GraphQLServer.class, TestConfig.class, EngineConfig.class})
public class GraphQLTest {

    private static final String PROCESS_KEY = "credit-application";
    private static final String BUSINESS_KEY = "0000000072";
    private static final String EXAMPLE_ID = "01234567";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private CustomerDataService customerDataServiceMock;

    @Autowired
    private GraphQLSchema graphQLSchema;

    @Autowired
    private ExecutionStrategy executionStrategy;

    @Test
    public void processDeployed() throws Exception {
        ProcessDefinition definition = processDefinition(PROCESS_KEY);
        assertThat(definition.getName()).isEqualTo("Credit Application");
    }

    @Test
    public void queryProcessInstance() throws Exception {
        final String query = "{ processInstances { id } }";
        //final String expectedResult = "{\"processInstances\":[{\"id\":\"1\"}]}";

        CustomerData customer = new CustomerData(EXAMPLE_ID, "Dummy Corp.", Personality.JURIDICAL, SolvencyRating.A);
        when(customerDataServiceMock.findById(EXAMPLE_ID)).thenReturn(customer);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY, startFormEntries(450000));
        ProcessEngineAssertions.assertThat(processInstance).isNotEnded();

        GraphQL graphQL = new GraphQL(graphQLSchema,executionStrategy);
        ExecutionResult executionResult = graphQL.execute(query);
        HashMap<String,Object> map = (HashMap<String,Object>)(executionResult.getData());

        // Expect No Errors
        assertThat(executionResult.getErrors().size()).isEqualTo(0);

        // Expect exactly one process instance
        assertThat(map.size()).isEqualTo(1);
        assertThat(map.keySet().toArray()[0]).isEqualTo("processInstances");

        String actualResult = JSON(map).toString();
        //@todo: use a JSON Validator? check out http://json-schema.org/implementations.html

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
