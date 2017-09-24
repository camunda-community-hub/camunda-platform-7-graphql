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

        ProcessEngine engine = BpmPlatform.getDefaultProcessEngine();
        if (engine == null) {
            engine = ProcessEngines.getDefaultProcessEngine(false);
        }
        return engine;
    }
}
