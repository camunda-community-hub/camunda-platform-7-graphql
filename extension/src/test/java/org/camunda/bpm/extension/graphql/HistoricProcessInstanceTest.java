package org.camunda.bpm.extension.graphql;

import graphql.ExecutionResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.graphql.infratest.bpm.BaseTest;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.WeatherCheckService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;

import static org.mockito.Mockito.when;

public class HistoricProcessInstanceTest extends BaseTest {

    private static final String PROCESS_KEY = "weather-process";
    private static final String BUSINESS_KEY = "552365";

    @Autowired
    private WeatherCheckService service;
    private ProcessInstance processInstance;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        scenario.load("historic-process-instance-scenarios.json");
        when(service.checkWeather()).thenReturn(30);
        processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, BUSINESS_KEY);

    }

    @Test
    public void shouldReturnTheHistoricProcessInstance() {

        final String query = "{ historicProcessInstances(businessKey:\""+ BUSINESS_KEY + "\" ) { id } }".replace("businessKey", BUSINESS_KEY);
        ExecutionResult executionResult = graphQL.execute(query);
        HashMap<String,Object> map = (HashMap<String,Object>)(executionResult.getData());

    }
}
