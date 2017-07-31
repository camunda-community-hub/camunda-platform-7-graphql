package org.camunda.bpm.extension.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.execution.ExecutionStrategy;
import graphql.execution.SimpleExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import org.camunda.bpm.engine.impl.persistence.entity.*;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.camunda.bpm.extension.graphql.types.ValueTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GraphQLServer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GraphQLServer.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GraphQLServer.class, args);
    }

    @Autowired
    private List<GraphQLResolver<?>> resolvers;

    @Bean
    public GraphQLSchema graphQLSchema() {
        return SchemaParser.newParser()
                .file("camunda.graphqls")
                .file("Execution.graphqls")
                .file("Task.graphqls")
                .file("User.graphqls")
                .file("ProcessDefinition.graphqls")
                .resolvers(resolvers)
                .dictionary(
                        Task.class,
                        TaskEntity.class,
                        ProcessInstance.class,
                        ProcessDefinition.class,
                        ProcessDefinitionEntity.class,
                        ExecutionEntity.class,
                        KeyValuePair.class,
                        UserEntity.class,
                        ValueTypeEnum.class
                )
                .build()
                .makeExecutableSchema();
    }

    @Bean
    ExecutionStrategy executionStrategy() {
        return new SimpleExecutionStrategy();
    }

    @Bean
    ServletRegistrationBean graphQLServletRegistrationBean(GraphQLSchema schema, ExecutionStrategy executionStrategy) {
        return new ServletRegistrationBean(new SimpleGraphQLServlet(schema, executionStrategy), "/");
    }
}
