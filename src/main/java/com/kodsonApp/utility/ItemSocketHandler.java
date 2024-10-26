package com.kodsonApp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemSocketHandler {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ItemSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastMessage(String message) {
        // Sends a message to all subscribers of /topic/pettycash-updates
        messagingTemplate.convertAndSend("/topic/items-updates", message);
    }
}
