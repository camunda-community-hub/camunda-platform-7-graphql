// this class needs to be in this package because of class loading
package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableProcessApplication("camunda-graphql")
public class GraphQLSpringBootServerStarter extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GraphQLSpringBootServerStarter.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GraphQLSpringBootServerStarter.class, args);
    }

}
