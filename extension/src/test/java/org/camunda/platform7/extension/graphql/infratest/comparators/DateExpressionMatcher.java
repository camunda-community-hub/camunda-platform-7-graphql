package org.camunda.platform7.extension.graphql.infratest.comparators;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.ValueMatcherException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;
import static org.camunda.platform7.extension.graphql.infratest.comparators.RangeGenerator.getRangeOf;

public class DateExpressionMatcher implements Comparators.Comparator {
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private final String path;

    public DateExpressionMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean equal(Object actual, Object expected) {
        try {
            new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(actual.toString());
            return TRUE;
        } catch (ParseException e) {
            throw new ValueMatcherException("Date pattern expected did not match value", "format expected " + DATE_FORMAT_PATTERN, actual.toString());
        }
    }

    public static List<Customization> isDate(String path) {
        return new DateExpressionMatcher(path).get();
    }

    @Override
    public List<Customization> get() {
        return getRangeOf(this.path).stream()
                .map(pathBuilt -> new Customization(pathBuilt, this)).collect(Collectors.toList());
    }
}
