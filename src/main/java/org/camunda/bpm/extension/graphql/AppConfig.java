package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EngineConfig.class})
public class AppConfig {

    @Bean
    public ProcessEngine processEngine() {

        ProcessEngine pe = BpmPlatform.getDefaultProcessEngine();
        if (pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        return pe;
    }
}
