package com.company.myappapi.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterSortRequest {
    private String field;
    private FilterOperation filterOperation;
    private String value;


    public FilterSortRequest(String field, String value) {
        this.field = field;
        this.value = value;
    }
}