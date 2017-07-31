package org.camunda.bpm.extension.graphql;


/**
 *
 */
public interface CustomerDataService {

    CustomerData findById(String customerId);

}
