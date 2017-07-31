package org.camunda.bpm.extension.graphql;

/**
 * Represents the customer's credit application.
 */
public class CreditApplication {

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
        final StringBuilder sb = new StringBuilder("CreditApplication{");
        sb.append("customerId='").append(customerId).append('\'');
        sb.append(", amountInEuro=").append(amountInEuro);
        sb.append(", interestRate=").append(interestRate);
        sb.append(", loanPeriodInMonth=").append(loanPeriodInMonth);
        sb.append('}');
        return sb.toString();
    }
}
