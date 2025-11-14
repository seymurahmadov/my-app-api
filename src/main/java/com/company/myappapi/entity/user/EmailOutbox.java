
package com.company.myappapi.entity.user;

import com.company.myappapi.enumaration.EmailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "email_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiverEmail;
    private String subject;

    @Column(columnDefinition="TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    private EmailStatus status = EmailStatus.PENDING;

    @Column(name = "attempt_count")
    private int attemptCount = 0;

    private LocalDateTime updatedAt= LocalDateTime.now();

}
