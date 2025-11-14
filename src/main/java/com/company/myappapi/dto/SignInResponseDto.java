package com.company.myappapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponseDto {
    private Long id;

    private String token;

    private String email;

    @JsonIgnore
    private String password;


    @Override
    public String toString() {
        return "SignInResponseDto{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
