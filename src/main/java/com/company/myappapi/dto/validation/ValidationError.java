package com.company.myappapi.dto.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    private String code;
    private String message;

    public static ValidationError of(String code, String message) {
        return new ValidationError(code, message);
    }
}
