package org.camunda.bpm.extension.graphql.infratest.bpm;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.extension.graphql.infratest.scenarios.ScenarioLoaderHelper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GraphQLServer.class, TestConfig.class})
public class BaseTest {

    protected GraphQL graphQL;

    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected GraphQLSchema graphQLSchema;

    protected ScenarioLoaderHelper scenario = new ScenarioLoaderHelper();

    @Before
    public void setUp() throws IOException {
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    protected String s(String scenarioName) {
        return this.scenario.getScenario(scenarioName).getJson();
    }

}
