package graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLController;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
    private List<GraphQLResolver> resolvers;

    @Bean
    public GraphQLSchema graphQLSchema() {
        return SchemaParser.newParser()
                .file("camunda.graphqls")
                .file("Execution.graphqls")
                .resolvers(resolvers)
                .dataClasses(TaskEntity.class, ProcessInstance.class, ProcessDefinition.class, ExecutionEntity.class, ProcessInstanceWithVariablesImpl.class)
                .build()
                .makeExecutableSchema();
    }

    @Bean
    GraphQLController graphQLController() {
        return new GraphQLController();
    }

}
