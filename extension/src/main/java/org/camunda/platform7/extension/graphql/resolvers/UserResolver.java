package org.camunda.platform7.extension.graphql.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class UserResolver implements GraphQLResolver<User> {

    private final static Logger LOGGER = Logger.getLogger(UserResolver.class.getName());

    @Autowired
    TaskService taskService;

    @Autowired
    IdentityService identityService;

    public UserResolver() {

    }

    public List<Task> tasks(User user) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskAssignee(user.getId());
        taskQuery.initializeFormKeys();
        return taskQuery.list();
    }

    public List<Group> groups(User user) {
        GroupQuery groupQuery = identityService.createGroupQuery();
        groupQuery.groupMember(user.getId());
        return groupQuery.list();
    }

}
