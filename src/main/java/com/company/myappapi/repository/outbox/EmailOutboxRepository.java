package com.company.myappapi.repository.outbox;

import com.company.myappapi.entity.user.EmailOutbox;
import com.company.myappapi.enumaration.EmailStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailOutboxRepository extends JpaRepository<EmailOutbox, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EmailOutbox e where e.status = 'PENDING'")
    List<EmailOutbox> findByStatus(EmailStatus status);
}