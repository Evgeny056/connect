package com.connectpublications.messagin.producer;

import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;

public interface MessageProducer {
    void sentMessageToNewPublicationQueue(NewPublicationBrokerDto newPublicationBrokerDto);
    void sentMessageNotificationNewPublication(NotificationFollowerBrokerDto notificationFollowerBrokerDto);
    void sentMessageNotificationNewComment(NewCommentBrokerDto newCommentBrokerDto);
    void sentMessageNewLike(LikeRequestDto likeRequestDto);
}
