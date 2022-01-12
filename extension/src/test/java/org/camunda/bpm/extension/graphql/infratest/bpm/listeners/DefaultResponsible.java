package org.camunda.bpm.extension.graphql.infratest.bpm.listeners;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.InstanceVariables;

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
