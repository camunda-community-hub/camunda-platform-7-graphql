package org.camunda.bpm.extension.graphql.infratest.bpm.services;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CustomerData;

/**
 *
 */
public interface CustomerDataService {

    CustomerData findById(String customerId);

}
