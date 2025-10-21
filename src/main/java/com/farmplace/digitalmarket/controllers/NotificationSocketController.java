package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.Model.Notifications;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToFarmer(Long farmerId, Notifications notifications){
        messagingTemplate.convertAndSend("/topic/notifications/"+farmerId,notifications);
    }
}
