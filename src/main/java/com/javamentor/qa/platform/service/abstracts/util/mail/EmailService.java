package com.javamentor.qa.platform.service.abstracts.util.mail;

public interface EmailService {
    void send(String from, String to, String subject, String text);
}
