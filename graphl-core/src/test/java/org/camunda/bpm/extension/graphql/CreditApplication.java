package org.camunda.bpm.extension.graphql;

import java.io.Serializable;

import static org.camunda.spin.Spin.JSON;

/**
 * Represents the customer's credit application.
 */
public class CreditApplication implements Serializable {

    private String customerId;
    private Long amountInEuro;
    private Double interestRate;
    private Long loanPeriodInMonth;

    public CreditApplication() {
    }

    public CreditApplication(String customerId, Long amountInEuro, Double interestRate, Long loanPeriodInMonth) {
        this.customerId = customerId;
        this.amountInEuro = amountInEuro;
        this.interestRate = interestRate;
        this.loanPeriodInMonth = loanPeriodInMonth;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getAmountInEuro() {
        return amountInEuro;
    }

    public void setAmountInEuro(Long amountInEuro) {
        this.amountInEuro = amountInEuro;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Long getLoanPeriodInMonth() {
        return loanPeriodInMonth;
    }

    public void setLoanPeriodInMonth(Long loanPeriodInMonth) {
        this.loanPeriodInMonth = loanPeriodInMonth;
    }

    public String toString() {
        return JSON(this).toString();
    }
}
