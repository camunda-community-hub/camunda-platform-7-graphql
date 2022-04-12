package org.camunda.platform7.extension.graphql.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class GroupResolver implements GraphQLResolver<Group> {
    private final static Logger LOGGER = Logger.getLogger(GroupResolver.class.getName());

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    IdentityService identityService;

    public GroupResolver() {
    }

    /**
     * TODO:: follow the rest api: https://docs.camunda.org/manual/7.5/reference/rest/group/get-query/
     *
     *
     * @param group
     * @return
     */

    public List<User> members (Group group) {

        String groupId = group.getId();
        LOGGER.info("groupId: " + groupId);

        if(groupId != null) {
            List<User> members = identityService.createUserQuery()
                    .memberOfGroup(groupId)
                    .list();
            return members;
        } else {
            return null;
        }


    }

}
