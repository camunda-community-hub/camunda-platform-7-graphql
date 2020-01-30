package org.camunda.bpm.extension.graphql;

/**
 * A customer's  credit-worthiness - the lower (i.e. A) the better.
 */
public enum SolvencyRating {

    A, B, C, D, E, F, G, H, I, J, K, L;

    public int getClassification() {
        return ordinal();
    }
}
