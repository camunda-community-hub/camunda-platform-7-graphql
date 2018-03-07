package org.camunda.bpm.extension.graphql;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transform single variable instances into "complex" object.
 */
public class CopyVariablesOnStart implements ExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(CopyVariablesOnStart.class);

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String customerId = (String) execution.getVariable(InstanceVariables.CUSTOMER_ID);
        Long amountInEuro = (Long) execution.getVariable(InstanceVariables.AMOUNT_IN_EURO);
        Double interestRate = Double.parseDouble((String) execution.getVariable(InstanceVariables.INTEREST_RATE));
        Long loanPeriod = (Long) execution.getVariable(InstanceVariables.LOAN_PERIOD);

        CreditApplication application = new CreditApplication(customerId, amountInEuro, interestRate, loanPeriod);

        execution.setVariable(InstanceVariables.CREDIT_APPLICATION, application);
        execution.setVariable("amountInEuro", application.getAmountInEuro());
        LOG.info("New credit application: {}", application);
    }
}
