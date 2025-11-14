package com.company.myappapi.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterOperation {
    EQUAL("EQUAL", "eq"),
    IN("IN", "in"),
    GREATER_THAN("GREATER_THAN", "gt"),
    LESS_THAN("LESS_THAN", "lt"),
    ENDS("ENDS", "ends"),
    BEGINS("BEGINS", "begins"),
    CONTAINS("CONTAINS", "ctn"),
    BETWEEN("BETWEEN", "between");

    private final String fullName;
    private final String shortName;

    FilterOperation(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    @JsonValue
    public String getFullName() {
        return fullName;
    }

    @JsonCreator
    public static FilterOperation fromValue(String value) {
        for (FilterOperation operation : FilterOperation.values()) {
            if (operation.fullName.equalsIgnoreCase(value) || operation.shortName.equalsIgnoreCase(value)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Invalid filter operation: " + value);
    }
}
