package com.connectpublications.messagin.consumer;

import com.rabbitmq.client.Channel;

public interface MessageListener {
    void handleNewPublicationQueue(String message);
    void notificationSubscribesNewPublication(String message, String routingKey);
    void handleNewCommentFollower(String message,String routingKey);
    void ownerAlerts(String message, String routingKey);
}
