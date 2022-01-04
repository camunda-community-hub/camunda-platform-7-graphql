package org.camunda.bpm.extension.graphql.infratest.bpm.dtos;

import java.util.Date;

import static org.camunda.spin.Spin.JSON;

/**
 * @author Stefan Schulze, PENTASYS AG
 * @since 15.02.2017
 */
public class CreditDecision {

    private final String resposible;
    private final boolean accepted;
    private final Date acceptedDate;

    public CreditDecision(String resposible, boolean accepted, Date acceptedDate) {

        this.resposible = resposible;
        this.accepted = accepted;
        this.acceptedDate = acceptedDate;
    }

    public String getResposible() {
        return resposible;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    @Override
    public String toString() {
        return JSON(this).toString();
    }
}
