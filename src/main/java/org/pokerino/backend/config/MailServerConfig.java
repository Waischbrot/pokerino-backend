package org.pokerino.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailServerConfig {
    @Value("${spring.mail.host}") private String emailHost;
    @Value("${spring.mail.port}") private int emailPort;
    @Value("${spring.mail.username}") private String emailUsername;
    @Value("${spring.mail.password}") private String emailPassword;

    @Bean
    public JavaMailSender javaMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailHost);
        mailSender.setPort(this.emailPort);
        mailSender.setUsername(this.emailUsername);
        mailSender.setPassword(this.emailPassword);
        final Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return mailSender;
    }
}