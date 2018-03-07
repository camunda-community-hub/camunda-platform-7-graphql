package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppConfig.class})
@ConditionalOnProperty(
        name = {"camunda.graphql.engine.resolver"},
        havingValue = "enabled",
        matchIfMissing = false
)
public class EngineConfig {

    @Bean
    @ConditionalOnMissingBean(name = "repositoryService")
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "runtimeService")
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "taskService")
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "historyService")
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "managementService")
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "identityService")
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authorizationService")
    public AuthorizationService authorizationService(ProcessEngine processEngine) {
        return processEngine.getAuthorizationService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "processEngineConfiguration")
    public ProcessEngineConfigurationImpl processEngineConfiguration(ProcessEngine processEngine) {
        return (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    }
}
