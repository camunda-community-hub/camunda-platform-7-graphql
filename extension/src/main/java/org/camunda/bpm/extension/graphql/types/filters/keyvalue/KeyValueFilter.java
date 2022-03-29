package org.camunda.bpm.extension.graphql.types.filters.keyvalue;

import java.util.Arrays;
import java.util.List;

public class KeyValueFilter {

    private KeyFilter key;


    public KeyFilter getKey() {
        if (key == null) return new KeyFilter(Arrays.asList());
        return key;
    }

    public void setKey(KeyFilter key) {
        this.key = key;
    }

    public static class KeyFilter {
        private List<String> in;

        public KeyFilter() {}

        public KeyFilter(List<String> in) {
            this.in = in;
        }

        public List<String> getIn() {
            return in;
        }

        public void setIn(List<String> in) {
            this.in = in;
        }
    }
}
