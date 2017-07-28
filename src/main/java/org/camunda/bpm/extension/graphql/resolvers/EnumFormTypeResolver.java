package org.camunda.bpm.extension.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.extension.graphql.types.KeyValuePair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by danielvogel on 30.06.17.
 */
@Component
public class EnumFormTypeResolver implements GraphQLResolver<EnumFormType> {

    public ArrayList<KeyValuePair> values (EnumFormType enumFormType) {
        Map<String, String> values = enumFormType.getValues();

        ArrayList<KeyValuePair> list = new ArrayList<>(values.size());

        for (Map.Entry<String, String> value: values.entrySet()) {
            KeyValuePair kvp = new KeyValuePair(value.getKey(), value.getValue(), null);
            list.add(kvp);
        }

        return list;
    }
}
