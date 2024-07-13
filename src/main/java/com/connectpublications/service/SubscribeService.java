package com.connectpublications.service;

import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;

public interface SubscribeService {
    void handleNewPublication(NewPublicationBrokerDto newPublicationBrokerDto);
}
