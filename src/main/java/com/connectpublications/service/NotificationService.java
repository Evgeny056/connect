package com.connectpublications.service;

import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;

public interface NotificationService {
    void handleNotificationNewPublication(NotificationFollowerBrokerDto notificationFollowerBrokerDto);
    void handleNotificationFollowersAddComment(NewCommentBrokerDto newCommentBrokerDto);
    void handleNotificationFollowersNewLike(LikeRequestDto likeRequestDto);

}
