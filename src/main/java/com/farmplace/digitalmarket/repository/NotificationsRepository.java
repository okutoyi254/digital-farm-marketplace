package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.Notifications;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends MongoRepository<Notifications,Long> {
}
