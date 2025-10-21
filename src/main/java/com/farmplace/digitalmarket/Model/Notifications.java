package com.farmplace.digitalmarket.Model;

import com.farmplace.digitalmarket.enums.NotificationStatus;
import com.farmplace.digitalmarket.enums.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "notifications")
public class Notifications {

    private LocalDateTime timeStamp;
    private Roles role;
    private String notificationMessage;
    private NotificationStatus notificationStatus;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;


}
