package org.camunda.bpm.extension.graphql;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 15.02.2017
 */
public interface DMSService {

    void publishDecision(CreditApplication application, CustomerData customerData, CreditDecision decision);

}
