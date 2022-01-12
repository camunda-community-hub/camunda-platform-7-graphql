package org.camunda.bpm.extension.graphql.infratest.bpm.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.InstanceVariables;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 26.05.2017
 */
public class UserResponsible implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        task.setVariable(InstanceVariables.RESPONSIBLE, task.getAssignee());
    }
}
