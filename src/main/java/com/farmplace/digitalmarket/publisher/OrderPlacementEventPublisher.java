package com.farmplace.digitalmarket.publisher;

import com.farmplace.digitalmarket.events.OrderPlacedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacementEventPublisher {

    private final ApplicationEventPublisher publisher;

    public OrderPlacementEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishOrderPlacedEvent(long userId,int quantity,String productName){
        publisher.publishEvent(new OrderPlacedEvent(this,userId,quantity,productName));
    }
}
