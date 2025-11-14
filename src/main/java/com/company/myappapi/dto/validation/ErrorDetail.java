package com.company.myappapi.dto.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ErrorDetail {
    private String title;
    private int status;
    private String detail;
    private long timeStamp;
    private Map<String, List<ValidationError>> errors;
}
