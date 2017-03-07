package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

@Component
public class Mutation extends GraphQLRootResolver {

    private ProcessEngine pe;
    private TaskService taskService;

    public Mutation() {
        super();
        pe = BpmPlatform.getDefaultProcessEngine();
        if(pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        taskService = pe.getTaskService();

    }

    public TaskEntity setAssignee(String taskEntityId, String assignee) {
        TaskEntity taskEntity = (TaskEntity)taskService.createTaskQuery().taskId(taskEntityId).singleResult();
        taskEntity.setAssignee(assignee);
        taskService.saveTask(taskEntity);
        return taskEntity;

    }
}
