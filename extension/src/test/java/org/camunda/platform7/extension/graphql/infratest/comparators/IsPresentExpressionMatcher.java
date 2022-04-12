package org.camunda.platform7.extension.graphql.infratest.comparators;


import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.ValueMatcherException;

import java.util.List;
import java.util.stream.Collectors;

import static org.camunda.platform7.extension.graphql.infratest.comparators.RangeGenerator.getRangeOf;

public class IsPresentExpressionMatcher implements Comparators.Comparator {
    private final String path;

    private IsPresentExpressionMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean equal(Object actual, Object expected) {
        if (actual != null && actual.toString().length() > 0) {
            return Boolean.TRUE;
        } else {
            throw new ValueMatcherException("The value is not present", actual.toString(), "");
        }
    }

    public static List<Customization> isPresent(String path) {
        return new IsPresentExpressionMatcher(path).get();
    }

    @Override
    public List<Customization> get() {
        return getRangeOf(this.path).stream()
                .map(pathBuilt -> new Customization(pathBuilt, this)).collect(Collectors.toList());
    }
}
