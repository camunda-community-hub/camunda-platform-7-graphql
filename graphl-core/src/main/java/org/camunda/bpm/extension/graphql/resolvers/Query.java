package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.EventSubscriptionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


@Component
public class Query implements GraphQLQueryResolver {

    private final static Logger LOGGER = Logger.getLogger(Query.class.getName());

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    public Query() {
    }

    public List<Task> tasks(String assignee, String name, String nameLike) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = (assignee != null) ? taskQuery.taskAssignee(assignee):taskQuery;
        taskQuery = (name != null) ? taskQuery.taskName(name):taskQuery;
        taskQuery = (nameLike != null) ? taskQuery.taskNameLike(nameLike):taskQuery;
        taskQuery.initializeFormKeys();
        return taskQuery.list();
    }

    public Task task(String id) {
        if (StringUtils.isEmpty(id)) return null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = taskQuery.taskId(id);
        taskQuery.initializeFormKeys();
        return taskQuery.singleResult();
    }

    public List<ProcessInstance> processInstances(String businessKey) {
    	ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        if (StringUtils.isNotEmpty(businessKey)) {
            processInstanceQuery.processInstanceBusinessKey(businessKey);
        }
        return processInstanceQuery.list();
    }

    public List<ProcessDefinition> processDefinitions(Boolean isSuspended, Boolean latest) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery = (BooleanUtils.isTrue(isSuspended)) ? processDefinitionQuery.suspended() : processDefinitionQuery;
        processDefinitionQuery = (BooleanUtils.isTrue(latest)) ? processDefinitionQuery.latestVersion() : processDefinitionQuery;
        return processDefinitionQuery.list();
    }

    public ProcessDefinition processDefinition(String id) {
        if (StringUtils.isEmpty(id)) return null;
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        return processDefinitionQuery.processDefinitionId(id).singleResult();
    }

    public List<KeyValuePair> taskVariables(String taskId, Collection<String> names ) {
        List<KeyValuePair> keyValuePairs;

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        if (processDefinitionId == null) {
            return null;
        }

        try {
            Util.switchContext(repositoryService, processDefinitionId, processEngineConfiguration);
            VariableMap variableMap = taskService.getVariablesTyped(taskId, names, true);
            keyValuePairs = Util.getKeyValuePairs(variableMap);
        } finally {
            ProcessApplicationContext.clear();
        }

        return keyValuePairs;
    }

    public List<Group> groups(String groupName, String groupNameLike, String groupType) {
        GroupQuery query = identityService.createGroupQuery();
        if (StringUtils.isNotEmpty(groupName)) {
            query.groupName(groupName);
        }
        if (StringUtils.isNotEmpty(groupNameLike)) {
            query.groupNameLike(groupNameLike);
        }
        if (StringUtils.isNotEmpty(groupType)) {
            query.groupType(groupType);
        }
        return query.list();
    }

    public Group group(String groupId) {
        if (StringUtils.isEmpty(groupId)) return null;
        return identityService.createGroupQuery().groupId(groupId).singleResult();
    }

    public List<User> users(String firstName, String firstNameLike, String groupId){
        UserQuery query = identityService.createUserQuery();
        if (StringUtils.isNotEmpty(firstName)) {
            query.userFirstName(firstName);
        }
        if (StringUtils.isNotEmpty(firstNameLike)) {
            query.userFirstNameLike(firstNameLike);
        }
        if (StringUtils.isNotEmpty(groupId)) {
            query.memberOfGroup(groupId);
        }
        return query.list();
    }

    public User user(String userId) {
        if (StringUtils.isEmpty(userId)) return null;
        return identityService.createUserQuery().userId(userId).singleResult();
    }

    public List<EventSubscription> eventSubscriptions(String type) {
        EventSubscriptionQuery eventSubscriptionQuery = runtimeService.createEventSubscriptionQuery();
        if (StringUtils.isNotEmpty(type)) {
            eventSubscriptionQuery.eventType(type);
        }
        return eventSubscriptionQuery.list();
    }
}
