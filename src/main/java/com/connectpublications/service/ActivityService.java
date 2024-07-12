package com.connectpublications.service;

import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;

public interface ActivityService {
    void notificationOwnerNewComment(NewCommentBrokerDto newCommentBrokerDto);
    void notificationOwnerNewLike(LikeRequestDto likeRequestDto);
}
