package org.camunda.bpm.extension.graphql.infratest.comparators;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static graphql.com.google.common.collect.Lists.newArrayList;
import static java.util.stream.IntStream.range;

public class RangeGenerator {

    public static List<String> getRangeOf(String path) {
        Pattern pattern = Pattern.compile("\\[\\d+,\\d+\\]");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String group = matcher.group(0);
            String[] split = group.replaceAll("(\\[|\\])", "").split(",");
            if (split.length > 2) {
                throw new RuntimeException("The range should have two values comma separated, example [0,4] from zero to four");
            }
            if (split.length == 1) {
                return newArrayList(path.replace(group, "[" + split[0] + "]"));
            }
            String firstIndex = split[0];
            String lastIndex = split[split.length - 1];
            return range(Integer.parseInt(firstIndex.trim()), Integer.parseInt(lastIndex.trim()))
                    .mapToObj(operand -> path.replace(group, "[" + operand + "]"))
                    .collect(Collectors.toList());
        }

        return newArrayList(path);
    }
}
