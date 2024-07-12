package com.connectpublications.messagin.producer;

import com.connectpublications.config.RabbitMQConfig;
import com.connectpublications.exception.ErrorConvertJsonStringException;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerRabbit implements MessageProducer {

    private static final String CONVERT_MESSAGE_ERROR = "An error occurred while converting an object to a string";

    private final RabbitTemplate rabbitTemplate;
    private final RabbitTemplate rabbitTemplatePublish;
    private final ObjectMapper jacksonObjectMapper;

    @Override
    public void sentMessageToNewPublicationQueue(NewPublicationBrokerDto newPublicationBrokerDto) {
        log.info("Sent new publication broker: {}", newPublicationBrokerDto);

        try {
            String messageBroker = jacksonObjectMapper.writeValueAsString(newPublicationBrokerDto);
            rabbitTemplatePublish.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_PUBLISHER, "newPublication", messageBroker );
        } catch (JsonProcessingException e) {
            throw new ErrorConvertJsonStringException(CONVERT_MESSAGE_ERROR);
        }
    }

    @Override
    public void sentMessageNotificationNewPublication(NotificationFollowerBrokerDto notificationFollowerBrokerDto) {
        log.info("Sending a notification about a new publication follower : {}", notificationFollowerBrokerDto.getFollowerId());

        try {
            String messageBroker = jacksonObjectMapper.writeValueAsString(notificationFollowerBrokerDto);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_NAME, "newPublication", messageBroker );
        } catch (JsonProcessingException e) {
            throw new ErrorConvertJsonStringException(CONVERT_MESSAGE_ERROR);
        }
    }

    @Override
    public void sentMessageNotificationNewComment(NewCommentBrokerDto newCommentBrokerDto) {
        log.info("Sending a notification about a new user comment AuthorId: {} to publication id: {}",
                newCommentBrokerDto.getAuthorComment(), newCommentBrokerDto.getPublicationId());
        try {
            String messageBroker = jacksonObjectMapper.writeValueAsString(newCommentBrokerDto);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_NAME, "newComment", messageBroker );
        } catch (JsonProcessingException e) {
            throw new ErrorConvertJsonStringException(CONVERT_MESSAGE_ERROR);
        }
    }

    @Override
    public void sentMessageNewLike(LikeRequestDto likeRequestDto) {
        log.info("\"Sending a notification about a new like to publication id: {}", likeRequestDto.getPublicationId());
        try {
            String messageBroker = jacksonObjectMapper.writeValueAsString(likeRequestDto);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_NAME, "newLike", messageBroker );
        } catch (JsonProcessingException e) {
            throw new ErrorConvertJsonStringException(CONVERT_MESSAGE_ERROR);
        }
    }
}
