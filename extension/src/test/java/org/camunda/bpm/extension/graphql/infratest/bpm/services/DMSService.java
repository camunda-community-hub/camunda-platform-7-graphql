package org.camunda.bpm.extension.graphql.infratest.bpm.services;

import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CreditApplication;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CreditDecision;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CustomerData;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 15.02.2017
 */
public interface DMSService {

    void publishDecision(CreditApplication application, CustomerData customerData, CreditDecision decision);

}
