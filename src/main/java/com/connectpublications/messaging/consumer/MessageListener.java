package com.connectpublications.messaging.consumer;

public interface MessageListener {
    void handleNewPublicationQueue(String message);
    void handleNotificationFollower(String message,String routingKey);
    void ownerAlerts(String message, String routingKey);
}
