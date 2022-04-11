package org.camunda.platform7.extension.graphql.types;

import graphql.schema.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DateTypeScalar {
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";


    @Bean
    public GraphQLScalarType date() {
        return GraphQLScalarType.newScalar().name("Date")
                .description("Date").coercing(new Coercing<Date, String>() {
                    @Override
                    public String serialize(Object date) throws CoercingSerializeException {
                        if (date == null) return StringUtils.EMPTY;
                        return new SimpleDateFormat(DATE_FORMAT_PATTERN).format((Date) date);
                    }
                    @Override
                    public Date parseValue(Object date) throws CoercingParseValueException {
                        if (date == null) return null;
                        try {
                            return new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(date.toString());
                        } catch (ParseException e) {
                            return null;
                        }
                    }
                    @Override
                    public Date parseLiteral(Object o) throws CoercingParseLiteralException {
                        return parseValue(o);
                    }
                }).build();

    }
}
