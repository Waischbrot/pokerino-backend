package org.pokerino.backend.application.port.in;

import org.springframework.messaging.MessagingException;

public interface SendMailUseCase {
    /**
     * Sends an email using the SMTP server
     * @param to recipient email address
     * @param subject subject line
     * @param text body / content of the email.
     * @throws MessagingException thrown if there has been a problem with sending the email
     */
    void sendMail(String to, String subject, String text) throws MessagingException;
}
