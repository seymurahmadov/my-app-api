package com.company.myappapi.entity.user.activity;

import com.company.myappapi.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "activity_log")
@NoArgsConstructor
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType action;

    @Enumerated(EnumType.STRING)
    private Module module;

    @Column(columnDefinition="TEXT")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime actionDate = LocalDateTime.now().withNano(0);

    public ActivityLog(String body, ActivityType action, Module module, User user) {
        this.body = body;
        this.action = action;
        this.module = module;
        this.user = user;
    }

    public static ActivityLog of(String body, ActivityType action, Module module, User user) {
        return new ActivityLog(body, action, module, user);
    }
}
