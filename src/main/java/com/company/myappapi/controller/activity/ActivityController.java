package com.company.myappapi.controller.activity;

import com.company.myappapi.dto.response.activity.ActivityLogResponse;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.service.user.activity.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityLogService activityLogService;

    public ActivityController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('ACTIVITY_LIST')")
    public ResponseEntity<Page<ActivityLogResponse>> findAll(@RequestParam Map<String, String> query) {
        return ResponseEntity.ok(activityLogService.findAllByPage(Converter.convert(query)));
    }
}
