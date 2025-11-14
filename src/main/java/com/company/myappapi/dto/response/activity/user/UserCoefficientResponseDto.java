package com.company.myappapi.dto.response.activity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserCoefficientResponseDto {
    private Long id;

    private String name;

    private String surname;

    private String email;

    private Boolean active;

    private String position;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private LocalDate employmentDate;

    private Long roleId;
    private String roleName;

    private Long generalManagerId;
    private String generalManagerName;

    private Long sectorId;
    private String sectorName;

    private Long unitId;
    private String unitName;

    private Long employeeId;
    private String employeeName;

    private Double coefficient;

}
