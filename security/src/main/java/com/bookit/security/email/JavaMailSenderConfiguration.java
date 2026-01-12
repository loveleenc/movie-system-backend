package com.bookit.security.email;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfiguration {

    @Bean
    public EmailProperties emailProperties(){
        return new EmailProperties();
    }


    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        EmailProperties emailProps = emailProperties();

        mailSender.setHost(emailProps.getHost());
        mailSender.setPort(emailProps.getPort());
        mailSender.setUsername(emailProps.getEmailUsername());
        mailSender.setPassword(emailProps.getEmailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
