package com.connectpublications.service.impl;

import com.connectpublications.exception.UserNotFoundException;
import com.connectpublications.messagin.producer.MessageProducer;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscribeService {

    private static final String AUTHOR_PUBLICATION_NOT_FOUND = "Author publication not found";

    private final UserRepository userRepository;
    private final MessageProducer messageProducer;

    @Transactional
    public void handleNewPublication(NewPublicationBrokerDto newPublicationBrokerDto) {

        User authorPublication = userRepository.findById(newPublicationBrokerDto.getAuthor().getUserId())
                .orElseThrow(() -> new UserNotFoundException(AUTHOR_PUBLICATION_NOT_FOUND));

        List<User> followers = authorPublication.getFollowers();

        for (User follower : followers) {
            NotificationFollowerBrokerDto notificationFollowerBrokerDto = new NotificationFollowerBrokerDto();
            notificationFollowerBrokerDto.setFollowerId(follower.getUserId());
            notificationFollowerBrokerDto.setMessage("Новая публикация от " + authorPublication.getFirstName() + " " +
                    authorPublication.getLastName());
            messageProducer.sentMessageNotificationNewPublication(notificationFollowerBrokerDto);
        }
    }
}
