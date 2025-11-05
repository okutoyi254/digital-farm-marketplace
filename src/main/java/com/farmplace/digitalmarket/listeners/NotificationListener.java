package com.farmplace.digitalmarket.listeners;

import com.farmplace.digitalmarket.Model.Notifications;
import com.farmplace.digitalmarket.Model.User;
import com.farmplace.digitalmarket.controllers.NotificationSocketController;
import com.farmplace.digitalmarket.enums.NotificationStatus;
import com.farmplace.digitalmarket.enums.Roles;
import com.farmplace.digitalmarket.events.OrderPlacedEvent;
import com.farmplace.digitalmarket.repository.NotificationsRepository;
import com.farmplace.digitalmarket.repository.UserRepository;
import com.farmplace.digitalmarket.utils.LoggedInCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@Slf4j

public class NotificationListener {


    private final UserRepository userRepository;
    private final NotificationsRepository repository;
    private final NotificationSocketController socketController;

    public NotificationListener(UserRepository userRepository, NotificationsRepository repository, NotificationSocketController socketController) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.socketController = socketController;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotificationSending(OrderPlacedEvent event){

        try {

            String message=String.format(
                    "You have a new order for %d units of %s.",
                    event.getQuantity(),
                    event.getProductName());

            Notifications notifications = Notifications.builder()
                    .userId(event.getUserId())
                    .notificationMessage(message)
                    .role(Roles.FARMER)
                    .notificationStatus(NotificationStatus.UNREAD)
                    .timeStamp(LocalDateTime.now()).build();
            Notifications saved = repository.save(notifications);

            socketController.sendNotificationToFarmer(event.getUserId(), saved);
        } catch (Exception ex){
            log.error("Failed to send notification: {}", ex.getMessage());
        }

    }
    }

