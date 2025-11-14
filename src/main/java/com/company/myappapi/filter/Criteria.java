package com.company.myappapi.filter;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Criteria {
    private final String field;
    private final FilterOperation operation;
    private String singleVal;
    private String minVal;
    private String maxVal;
    private List<String>     inVals;

    public Criteria(String field, String value) {
        this.field = field;
        String[] sp = StringUtils.split(value, ":");

        this.operation = FilterOperation.fromValue(sp[0]);

        if (this.operation.equals(FilterOperation.BETWEEN)) {
            String[] s = sp[1].split(",");
            this.minVal = s[0];
            this.maxVal = s[1];
        } else if (this.operation.equals(FilterOperation.IN)) {
            this.inVals = Arrays.asList(sp[1].split(","));
        } else {
            this.singleVal = sp[1];
        }
    }

    public static Criteria of(String field, String value) {
        return new Criteria(field, value);
    }

    public String getField() {
        return field;
    }

    public FilterOperation getOperation() {
        return operation;
    }

    public <T> T getSingleVal(Function<String, T> func) {
        return func.apply(singleVal);
    }

    public <T> T getMinVal(Function<String, T> func) {
        return func.apply(minVal);
    }

    public <T> T getMaxVal(Function<String, T> func) {
        return func.apply(maxVal);
    }

    public <T> List<T> getInVals(Function<String, T> func) {
        return inVals.stream()
                .map(func)
                .collect(Collectors.toList());
    }
}
