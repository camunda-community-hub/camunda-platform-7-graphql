package org.camunda.platform7.extension.graphql.infratest.comparators;


import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.ValueMatcherException;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.camunda.platform7.extension.graphql.infratest.comparators.RangeGenerator.getRangeOf;

public class IsANumberExpressionMatcher implements Comparators.Comparator {
    private final String path;

    private IsANumberExpressionMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean equal(Object actual, Object expected) {

        if (isDigits(actual.toString())) {
            return Boolean.TRUE;
        } else {
            throw new ValueMatcherException("The value expected should be a number", actual.toString(), "is not a numeric value");
        }
    }

    public static List<Customization> isNumeric(String path) {
        return new IsANumberExpressionMatcher(path).get();
    }

    @Override
    public List<Customization> get() {
        return getRangeOf(this.path).stream()
                .map(pathBuilt -> new Customization(pathBuilt, this)).collect(Collectors.toList());
    }
}
