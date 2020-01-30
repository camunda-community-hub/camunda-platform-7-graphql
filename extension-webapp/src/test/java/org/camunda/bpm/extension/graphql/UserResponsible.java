package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

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
