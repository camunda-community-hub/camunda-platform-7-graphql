package org.camunda.bpm.extension.graphql.types;

import org.springframework.stereotype.Component;

//@todo: implement next
@Component
public class KeyValuePair {

    private String key = null;
    private String value = null;
    private ValueTypeEnum valueType = null;

    public KeyValuePair() {

	}

	public KeyValuePair(String key, String value, ValueTypeEnum valueType) {
		this.key = key;
		this.value = value;
		this.valueType = valueType;
	}

	public ValueTypeEnum getValueType() {
		return valueType;
	}

	public void setValueType(ValueTypeEnum valueType) {
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
