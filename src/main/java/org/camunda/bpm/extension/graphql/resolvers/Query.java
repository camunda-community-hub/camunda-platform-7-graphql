package org.camunda.bpm.extension.graphql.resolvers;

import java.util.List;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

@Component
public class Query implements GraphQLRootResolver {

    private ProcessEngine pe;

    private TaskService taskService;
    private RuntimeService runtimeService;
    private IdentityService identityService;

    public Query() {
        super();
        pe = BpmPlatform.getDefaultProcessEngine();
        if(pe == null) {
            pe = ProcessEngines.getDefaultProcessEngine(false);
        }
        taskService = pe.getTaskService();
        runtimeService = pe.getRuntimeService();
        identityService = pe.getIdentityService();
    }

    public List<Task> tasks(String assignee, String name, String nameLike) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = (assignee != null) ? taskQuery.taskAssignee(assignee):taskQuery;
        taskQuery = (name != null) ? taskQuery.taskName(name):taskQuery;
        taskQuery = (nameLike != null) ? taskQuery.taskNameLike(nameLike):taskQuery;
        taskQuery.initializeFormKeys();
        List<Task> tasks = taskQuery.list();
        return tasks;
    }

    public Task task(String id) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = taskQuery.taskId(id);
        taskQuery.initializeFormKeys();
        Task task = taskQuery.singleResult();
        return task;
    }

    public List<ProcessInstance> processesInstances(String businessKey) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        processInstanceQuery = (businessKey != null) ? processInstanceQuery.processInstanceBusinessKey(businessKey):processInstanceQuery;
        List<ProcessInstance> processInstances = processInstanceQuery.list();
        return processInstances;
    }
 
    public ProcessInstance processInstance(String id, String businessKey){
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        processInstanceQuery = (businessKey != null) ? processInstanceQuery.processInstanceBusinessKey(businessKey):processInstanceQuery;
        ProcessInstance processInstance = processInstanceQuery.processInstanceId(id).singleResult();
        return processInstance;
    }
    
    public List<Group> groups(String groupName, String groupNameLike, String groupType) {
        GroupQuery query = identityService.createGroupQuery();
        query = (groupName != null)      ? query.groupName(groupName)          : query;
        query = (groupNameLike != null)  ? query.groupNameLike(groupNameLike)  : query;
        query = (groupType != null)      ? query.groupType(groupType)          : query;
        List<Group> groups = query.list();
        return groups;
    }
    
    public Group group(String groupId) {
        if(groupId != null) {
            Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            return group;
        } else {
            return null;
        }
    }
    
    public List<User> users(String firstName, String firstNameLike, String groupId){
        UserQuery query = identityService.createUserQuery();
        query = (firstName != null)      ? query.userFirstName(firstName)           : query;
        query = (firstNameLike != null)  ? query.userFirstNameLike(firstNameLike)   : query;  
        query = (groupId != null)  ? query.memberOfGroup(groupId)       : query;
        List<User> users = query.list();
        
        return users;
    }
    
    public User user(String userId) {
        if(userId != null) {
            User user = identityService.createUserQuery().userId(userId).singleResult();
            return user;
        } else {
            return null;
        }
    }
        
    
}
