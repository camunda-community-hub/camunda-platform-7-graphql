package org.camunda.bpm.extension.graphql;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import graphql.schema.GraphQLSchema;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class, EngineConfig.class})
@SpringBootTest
public class GraphQLTest {

    private static final String PROCESS_KEY = "credit-application";
    private static final String EXAMPLE_ID = "01234567";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private CustomerDataService customerDataServiceMock;

    @Test
    public void processDeployed() throws Exception {
        ProcessDefinition definition = processDefinition(PROCESS_KEY);
        assertThat(definition.getName()).isEqualTo("Credit Application");
    }

    @Test
    public void queryTask() throws Exception {
        CustomerData customer = new CustomerData(EXAMPLE_ID, "Dummy Corp.", Personality.JURIDICAL, SolvencyRating.A);
        when(customerDataServiceMock.findById(EXAMPLE_ID)).thenReturn(customer);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, startFormEntries(450000));
        ProcessEngineAssertions.assertThat(processInstance).isNotEnded();

        // @todo: query GraphQL endpoint...
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
