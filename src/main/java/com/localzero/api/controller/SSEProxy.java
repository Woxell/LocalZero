package com.localzero.api.controller;

import com.localzero.api.Logger;
import com.localzero.api.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SSEProxy {

    private final SSEController sseController;
    private final PersonService personService;
    private final Logger logger = Logger.getInstance();

    public void authenticateAndNotifyReceiver(String receiverEmail, Object message) {
        if (authenticateReceiver(receiverEmail)) {
            sseController.sendMessageToReceiver(receiverEmail, message);
        } else {
            logger.logError("Unauthorized access attempt by: " + receiverEmail);
        }
    }

    private boolean authenticateReceiver(String email) {
        return personService.findByEmail(email) != null;
    }
}
