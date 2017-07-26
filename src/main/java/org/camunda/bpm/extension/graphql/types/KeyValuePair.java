package org.camunda.bpm.extension.graphql.types;

import org.springframework.stereotype.Component;

//@todo: implement next
@Component
public class KeyValuePair {

    private String key = null;
    private String value = null;
    private String valueType = null;

    public KeyValuePair() {

	}

	public KeyValuePair(String key, String value, String valueType) {
		this.key = key;
		this.value = value;
		this.valueType = valueType;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
