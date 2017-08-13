package org.camunda.bpm.extension.graphql.resolvers;

import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

@Component
public class GroupEntityResolver implements GraphQLResolver<GroupEntity> {
    private final static Logger LOGGER = Logger.getLogger("GroupEntityResolver");

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    IdentityService identityService;

    public GroupEntityResolver() {
    }

    /**
     * TODO:: follow the rest api: https://docs.camunda.org/manual/7.5/reference/rest/group/get-query/
     *
     *
     * @param nameLike
     * @param type
     * @return
     */

    public List<User> members (GroupEntity groupEntity) {

        String groupId = groupEntity.getId();
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