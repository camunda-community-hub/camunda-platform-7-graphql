package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.LaneSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 26.05.2017
 */
public class GetSwimlaneGroup implements TaskListener {

    private static final Logger LOG = LoggerFactory.getLogger(GetSwimlaneGroup.class);

    @Override
    public void notify(DelegateTask task) {
        String group = determineGroupFromLane(task);
        task.addCandidateGroup(group);
        LOG.info("Assigned to '{}'", group);
    }

    private String determineGroupFromLane(DelegateTask task) {
        Collection<LaneSet> laneSets = task.getBpmnModelInstance().getModelElementsByType(LaneSet.class);
        return laneSets.stream()
            .flatMap(ls -> ls.getLanes().stream())
            .filter(l -> containsFlowNode(l, task))
            .map(Lane::getName)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No lane found"));
    }

    private boolean containsFlowNode(Lane l, DelegateTask task) {
        return l.getFlowNodeRefs()
            .stream()
            .anyMatch(fn -> task.getTaskDefinitionKey().equals(fn.getId()));
    }


}
