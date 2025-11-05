package com.farmplace.digitalmarket.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {

    private final long userId;
    private final int quantity;
    private final String productName;

    public OrderPlacedEvent(Object source, long userId, int quantity, String productName) {

        super(source);
        this.userId = userId;
        this.quantity = quantity;
        this.productName = productName;
    }
}
