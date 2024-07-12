package com.connectpublications.service;

import com.connectpublications.model.dto.request.SubscribeRequestDto;

public interface CreateSubscribeService {
    void followUser(SubscribeRequestDto subscribeRequestDto);
}
