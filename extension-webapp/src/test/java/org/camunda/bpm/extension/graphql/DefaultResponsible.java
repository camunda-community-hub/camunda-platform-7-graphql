package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 26.05.2017
 */
public class DefaultResponsible implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable(InstanceVariables.RESPONSIBLE, "Business Rule");
    }
}
