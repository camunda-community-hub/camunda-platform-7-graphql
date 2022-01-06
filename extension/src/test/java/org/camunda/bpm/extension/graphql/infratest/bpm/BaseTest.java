package org.camunda.bpm.extension.graphql.infratest.bpm;

import com.jayway.jsonpath.JsonPath;
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
    public void setUp() throws Exception {
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    protected String query(String scenarioName) {
        return JsonPath.read(this.scenario.getScenario(scenarioName).getJson(), "$.query");
    }

    protected String s(String scenarioName) {
        return this.scenario.getScenario(scenarioName).getJson();
    }

}
