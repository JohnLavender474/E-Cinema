package com.ecinema.app.services;

import com.ecinema.app.exceptions.EmailException;

public interface EmailSenderService {
    void sendFromBusinessEmail(String to, String email, String subject)
            throws EmailException;
    void send(String from, String to, String email, String subject)
            throws EmailException;
}
