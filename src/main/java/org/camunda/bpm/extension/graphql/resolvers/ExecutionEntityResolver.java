package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.stereotype.Component;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;

@Component
public class ExecutionEntityResolver implements GraphQLResolver<ExecutionEntity> {

    private ProcessEngine pe;

    private TaskService taskService;
    private RuntimeService runtimeService;

    public ExecutionEntityResolver() {
    	 pe = BpmPlatform.getDefaultProcessEngine();
         if(pe == null) {
             pe = ProcessEngines.getDefaultProcessEngine(false);
         }
         taskService = pe.getTaskService();
         runtimeService = pe.getRuntimeService();
    }

    public List<KeyValuePair> variables(ProcessInstance pi) {
        
        ArrayList<KeyValuePair> kvps = new ArrayList<KeyValuePair>();
        
        VariableMap variables = null;
		variables = runtimeService.getVariablesTyped(pi.getId());
		
		for(String name: variables.keySet()) {
			KeyValuePair kvp = new KeyValuePair();
			String type;
			
		     kvp.setKey(name);
		     
		     TypedValue variable = variables.getValueTyped(name);
		     
		     type = variable.getType().getName();
		     if (type != null) {
		    	 kvp.setValueType(type.substring(0, 1).toUpperCase() + type.substring(1));
		     }
		     
		     kvp.setValue(variable.getValue().toString());
		     
		     kvps.add(kvp);
		    }
		
        return kvps;
    }
}
