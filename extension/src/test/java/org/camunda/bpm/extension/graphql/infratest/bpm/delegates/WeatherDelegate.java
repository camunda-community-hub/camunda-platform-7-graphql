package org.camunda.bpm.extension.graphql.infratest.bpm.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.InstanceVariables;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.WeatherCheckService;
import org.springframework.stereotype.Component;

@Component
public class WeatherDelegate implements JavaDelegate {

    private final WeatherCheckService service;

    public WeatherDelegate(WeatherCheckService service) {
        this.service = service;
    }

    public void execute(DelegateExecution execution) throws Exception {
        Integer temperature = service.checkWeather();
        execution.setVariable(InstanceVariables.TEMPERATURE, temperature);
    }
}
