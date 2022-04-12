package org.camunda.platform7.extension.graphql.infratest.comparators;


import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.ValueMatcherException;

import java.util.List;
import java.util.stream.Collectors;

public class StringSizeExpressionMatcher implements Comparators.Comparator {
    private final Integer size;
    private final String path;

    private StringSizeExpressionMatcher(Integer size, String path) {
        this.size = size;
        this.path = path;
    }

    @Override
    public boolean equal(Object actual, Object expected) {
        if (actual.toString().length() == size) {
            return Boolean.TRUE;
        } else {
            throw new ValueMatcherException("String size expected did not match value", this.size + "", actual.toString().length() + "");
        }
    }

    public static List<Customization> stringSize(Integer size, String path) {
        return new StringSizeExpressionMatcher(size, path).get();
    }

    @Override
    public List<Customization> get() {
        return RangeGenerator.getRangeOf(this.path).stream()
                .map(pathBuilt -> new Customization(pathBuilt, this)).collect(Collectors.toList());
    }
}
