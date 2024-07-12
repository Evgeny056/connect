package com.connectpublications.messagin.consumer;

public interface MessageListener {
    void handleNewPublicationQueue(String message);
    void notificationSubscribesNewPublication(String message, String routingKey);
    void handleNewCommentFollower(String message,String routingKey);
    void ownerAlerts(String message, String routingKey);
}
