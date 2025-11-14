package com.company.myappapi.dto.response.activity;

import com.company.myappapi.entity.user.activity.ActivityType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityLogResponse {
    private final Long id;
    private final ActivityType action;
    private final Module module;
    private final String body;
    private final String nameSurname;
    private final LocalDateTime actionDate;

    public ActivityLogResponse(Long id, ActivityType action, Module module, String body, String nameSurname, LocalDateTime actionDate) {
        this.id = id;
        this.action = action;
        this.module = module;
        this.body = body;
        this.nameSurname = nameSurname;
        this.actionDate = actionDate;
    }
}
