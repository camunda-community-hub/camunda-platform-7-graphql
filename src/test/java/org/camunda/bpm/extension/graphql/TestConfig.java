package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.jobexecutor.DefaultJobExecutor;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Configuration
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration testProcessEngineConfiguration() {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setProcessEngineName("default");
        conf.setDataSource(dataSource());
        conf.setTransactionManager(transactionManager());
        conf.setDatabaseSchemaUpdate("true");
        conf.setDeploymentResources(deploymentResources());
        conf.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
        conf.setJobExecutor(jobExecutor());
        conf.setProcessEnginePlugins(processEnginePlugins());
        return conf;
    }

    @Bean
    public JobExecutor jobExecutor() {
        JobExecutor jobExecutor = new DefaultJobExecutor();
        jobExecutor.setWaitTimeInMillis(100);
        return jobExecutor;
    }


    @Bean
    public List<ProcessEnginePlugin> processEnginePlugins() {
        return Collections.singletonList(
                new SpringProcessEnginePlugin()
        );
    }

    @Bean
    public Resource[] deploymentResources() {
        return new Resource[]{
                new ClassPathResource("processes/check-risk.dmn"),
                new ClassPathResource("processes/credit-application.bpmn")};
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(testProcessEngineConfiguration());
        return factoryBean;
    }

    @Bean
    public ProcessEngine processEngine() {
        try {
            return processEngineFactoryBean().getObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ProcessEngine", e);
        }
    }

    @Bean
    public CustomerDataService customerDataService() {
        return Mockito.mock(CustomerDataService.class);
    }

    @Bean
    public CopyVariablesOnStart copyVariablesOnStart() {
        return new CopyVariablesOnStart();
    }

    @Bean
    public RetrieveCustomerData retrieveCustomerData(CustomerDataService service) {
        return new RetrieveCustomerData(service);
    }

    @Bean
    public DMSService dmsService() {
        return Mockito.mock(DMSService.class);
    }

    @Bean
    public PublishDecision publishDecision(DMSService service) {
        return new PublishDecision(service);
    }

    @Bean
    public DefaultResponsible defaultResponsible() {
        return new DefaultResponsible();
    }

    @Bean
    public UserResponsible userResponsible() {
        return new UserResponsible();
    }

    @Bean
    public GetSwimlaneGroup getSwimlaneGroup() {
        return new GetSwimlaneGroup();
    }

}
