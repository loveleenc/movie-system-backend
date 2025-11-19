package com.bookit.application.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailProperties emailProperties;

    public void sendEmail(String toEmail, String subject, String message) throws MailSendException, MailAuthenticationException, MailParseException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailProperties.getEmailUsername());
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }


}
