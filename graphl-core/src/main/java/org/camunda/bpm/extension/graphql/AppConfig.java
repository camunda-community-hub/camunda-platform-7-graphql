package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(
        name = {"camunda.graphql.engine.resolver"},
        havingValue = "enabled",
        matchIfMissing = false
)
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
