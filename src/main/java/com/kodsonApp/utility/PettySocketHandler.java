package com.kodsonApp.utility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class PettySocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PettySocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastMessage(String message) {
        // Sends a message to all subscribers of /topic/pettycash-updates
        messagingTemplate.convertAndSend("/topic/pettycash-updates", message);
    }
}

