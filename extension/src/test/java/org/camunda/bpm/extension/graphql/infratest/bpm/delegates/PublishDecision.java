package org.camunda.bpm.extension.graphql.infratest.bpm.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CreditApplication;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CreditDecision;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.CustomerData;
import org.camunda.bpm.extension.graphql.infratest.bpm.dtos.InstanceVariables;
import org.camunda.bpm.extension.graphql.infratest.bpm.services.DMSService;

import java.util.Date;

import static org.camunda.spin.Spin.JSON;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 26.05.2017
 */
public class PublishDecision implements JavaDelegate {

    private final DMSService dmsService;

    public PublishDecision(DMSService dmsService) {
        this.dmsService = dmsService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String decision = (String) execution.getVariable(InstanceVariables.DECISION);
        String responsible = (String) execution.getVariable(InstanceVariables.RESPONSIBLE);
        CreditApplication application = JSON(execution.getVariable(InstanceVariables.CREDIT_APPLICATION).toString()).mapTo(CreditApplication.class);
        CustomerData customer = JSON(execution.getVariable(InstanceVariables.CUSTOMER_DATA).toString()).mapTo(CustomerData.class);
        boolean accepted = "ACCEPT".equals(decision);

        CreditDecision creditDecision = new CreditDecision(responsible, accepted, new Date());
        dmsService.publishDecision(application, customer, creditDecision);
    }
}
