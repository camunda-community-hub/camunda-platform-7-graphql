package org.camunda.bpm.extension.graphql.types;


import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.*;

/**
 * Created by danielvogel on 23.06.17.
 */

public enum ValueTypeEnum {
    OBJECT,
    INT,
    FLOAT, /* camunda has no float type? */
    STRING,
    BOOLEAN,
    LONG,
    DOUBLE;

    public static ValueTypeEnum from(ValueType type) {
        switch (type.getName()) {
            case "object": return OBJECT;
            case "int": return INT;
            case "long": return LONG;
            case "double": return DOUBLE;
            case "string": return STRING;
            case "boolean": return BOOLEAN;
            default: break;
        }
        return null;
    }
}