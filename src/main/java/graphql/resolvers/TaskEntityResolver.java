package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityResolver extends GraphQLResolver {

    private ProcessEngine pe;
    private RepositoryService repositoryService;

    public TaskEntityResolver() {
        super(TaskEntity.class);
        pe = BpmPlatform.getDefaultProcessEngine();
        if (pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        repositoryService = pe.getRepositoryService();
    }

    public ProcessDefinition processDefinition(TaskEntity taskEntity) {
        String pdid = taskEntity.getProcessDefinitionId();
        if (pdid != null) {
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(pdid);
            return processDefinition;
        } else
            return null;
    }

    public ExecutionEntity executionEntity(TaskEntity taskEntity) {
        //@todo: implement this!
        return null;
    }
}
