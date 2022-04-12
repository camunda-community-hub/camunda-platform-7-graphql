package org.camunda.platform7.extension.graphql.infratest.bpm.services;
import org.camunda.platform7.extension.graphql.infratest.bpm.dtos.CustomerData;

/**
 *
 */
public interface CustomerDataService {

    CustomerData findById(String customerId);

}
