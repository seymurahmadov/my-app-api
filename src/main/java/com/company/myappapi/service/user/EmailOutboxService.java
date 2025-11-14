//package com.company.myappapi.service.user;
//
//
//import com.company.myappapi.entity.user.EmailOutbox;
//import com.company.myappapi.enumaration.EmailStatus;
//import com.company.myappapi.repository.outbox.EmailOutboxRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class EmailOutboxService {
//    @Value("${spring.mail.username}")
//    private String fromMail;
//
//    private final JavaMailSender mailSender;
//    private final EmailOutboxRepository outboxRepository;
//
//    public EmailOutboxService(JavaMailSender mailSender, EmailOutboxRepository outboxRepository) {
//        this.mailSender = mailSender;
//        this.outboxRepository = outboxRepository;
//    }
//
//    @Transactional
//    @Scheduled(fixedDelay = 60000) // 1 dəqiqədən bir çalışır
//    public void processOutboxEmails() {
//        List<EmailOutbox> pendingEmails = outboxRepository.findByStatus(EmailStatus.PENDING);
//
//        for (EmailOutbox email : pendingEmails) {
//            try {
//                sendEmail(email);
//                email.setStatus(EmailStatus.SENT);
//            } catch (Exception e) {
//                email.setAttemptCount(email.getAttemptCount() + 1);
//                if (email.getAttemptCount() >= 3) {
//                    email.setStatus(EmailStatus.FAILED);
//                }
//            }
//            outboxRepository.save(email);
//        }
//    }
//
//    private void sendEmail(EmailOutbox email) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email.getReceiverEmail());
//        message.setFrom(fromMail);
//        message.setSubject(email.getSubject());
//        message.setText(email.getBody());
//        mailSender.send(message);
//    }
//}
