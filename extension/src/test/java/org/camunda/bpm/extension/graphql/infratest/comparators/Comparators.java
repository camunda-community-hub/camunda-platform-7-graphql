package org.camunda.bpm.extension.graphql.infratest.comparators;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Comparators {

    public static CustomComparator comparators(JSONCompareMode mode, List<Customization>... customizations) {
        List<Customization> customizationsList = stream(customizations).flatMap(List::stream).collect(Collectors.toList());

        return new CustomComparator(mode, customizationsList.toArray(new Customization[customizationsList.size()]));
    }

    public interface Comparator extends ValueMatcher<Object> {

        List<Customization> get();
    }
}
