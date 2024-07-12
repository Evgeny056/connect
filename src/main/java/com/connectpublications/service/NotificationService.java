package com.connectpublications.service;

import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;

public interface NotificationService {
    void handleNotificationNewPublication(NotificationFollowerBrokerDto notificationFollowerBrokerDto);
    void handleNotificationFollowersAddComment(NewCommentBrokerDto newCommentBrokerDto);
}
