package com.ecinema.app.services.implementations;

import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.services.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String BUSINESS_EMAIL = "csci4050.b7.ecinema@gmail.com";

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendFromBusinessEmail(String to, String email, String subject)
            throws EmailException {
        send(BUSINESS_EMAIL, to, email, subject);
    }

    @Override
    public void send(String from, String to, String email, String subject)
            throws EmailException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setFrom(BUSINESS_EMAIL);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailException(e.getMessage());
        }
    }

    @Override
    public String getBusinessEmail() {
        return BUSINESS_EMAIL;
    }

}
