package com.connectpublications.messagin.consumer;

import com.connectpublications.config.RabbitMQConfig;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.service.ActivityService;
import com.connectpublications.service.NotificationService;
import com.connectpublications.service.impl.SubscriptionsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRabbit implements MessageListener {

    private final ObjectMapper jacksonObjectMapper;
    private final SubscriptionsService subscriptionsService;
    private final NotificationService notificationService;
    private final ActivityService activityService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.NEW_PUBLICATIONS_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.DIRECT_EXCHANGE_PUBLISHER),
            key = "newPublication"))
    public void handleNewPublicationQueue(String message) {

        try {
            NewPublicationBrokerDto newPublicationBrokerDto = jacksonObjectMapper.readValue(message, NewPublicationBrokerDto.class);
            log.info("Received message from NewPublicationQueue: {}", newPublicationBrokerDto.toString());
            subscriptionsService.handleNewPublication(newPublicationBrokerDto);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON message: {}", e.getMessage());
        }
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.SUBSCRIBER_NOTIFICATIONS_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.DIRECT_EXCHANGE_NAME),
            key = "newPublication"))
    public void notificationSubscribesNewPublication(String message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
       if (routingKey.equalsIgnoreCase("newPublication")) {
           try {
               NotificationFollowerBrokerDto notificationFollowerBrokerDto =
                       jacksonObjectMapper.readValue(message, NotificationFollowerBrokerDto.class);
               log.info("A message was received from the new publications queue: {}", notificationFollowerBrokerDto.toString());
               notificationService.handleNotificationNewPublication(notificationFollowerBrokerDto);
           } catch (JsonProcessingException e) {
               log.error("Error parsing JSON message: {}", e.getMessage());
           }
       }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.SUBSCRIBER_NOTIFICATIONS_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.DIRECT_EXCHANGE_NAME),
            key = "newComment"))
    public void handleNewCommentFollower(String message,@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if (routingKey.equalsIgnoreCase("newComment")) {
            try {
                NewCommentBrokerDto newCommentBrokerDto = jacksonObjectMapper.readValue(message, NewCommentBrokerDto.class);
                log.info("Received a message about a new comment: {}", newCommentBrokerDto.toString());
                notificationService.handleNotificationFollowersAddComment(newCommentBrokerDto);
            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON message: {}", e.getMessage());
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.SUBSCRIBER_NOTIFICATIONS_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.DIRECT_EXCHANGE_NAME),
            key = {"newComment", "newLike"}))
    public void ownerAlerts(String message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if (routingKey.equalsIgnoreCase("newComment")) {
            try {
                NewCommentBrokerDto newCommentBrokerDto = jacksonObjectMapper.readValue(message, NewCommentBrokerDto.class);
                log.info("Sending a message about a new comment to the owner from {} {}",
                        newCommentBrokerDto.getAuthorComment().getFirstName(), newCommentBrokerDto.getAuthorComment().getLastName());

                activityService.notificationOwnerNewComment(newCommentBrokerDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        if(routingKey.equalsIgnoreCase("newLike")) {
            try {
                LikeRequestDto likeRequestDto = jacksonObjectMapper.readValue(message, LikeRequestDto.class);
                log.info("Sending a message about a new like to the owner from {} {}",
                        likeRequestDto.getAuthor().getFirstName(), likeRequestDto.getAuthor().getLastName());

                activityService.notificationOwnerNewLike(likeRequestDto);
            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON message: {}", e.getMessage());
            }
        }
    }

}
