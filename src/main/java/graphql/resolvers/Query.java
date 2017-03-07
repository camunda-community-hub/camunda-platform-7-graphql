package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query extends GraphQLRootResolver {

    private ProcessEngine pe;

    private TaskService taskService;
    private RuntimeService runtimeService;

    public Query() {
        super();
        pe = BpmPlatform.getDefaultProcessEngine();
        if(pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        taskService = pe.getTaskService();
        runtimeService = pe.getRuntimeService();
    }

    public List<Task> tasks(String assignee) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = (assignee != null) ? taskQuery.taskAssignee(assignee):taskQuery;
        List<Task> tasks = taskQuery.list();
        return tasks;
    }

    public List<ProcessInstance> processes() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        return processInstances;
    }
}
