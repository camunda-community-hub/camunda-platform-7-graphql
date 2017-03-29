package org.camunda.bpm.extension.graphql.resolvers;

import org.springframework.stereotype.Component;

//@todo: implement next
@Component
public class TaskVariable {

    private String key;
    private String value;


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
