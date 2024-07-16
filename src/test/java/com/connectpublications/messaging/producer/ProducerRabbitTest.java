package com.connectpublications.messaging.producer;

import com.connectpublications.config.RabbitMQConfig;
import com.connectpublications.config.TestConfigurationConfig;
import com.connectpublications.model.dto.UserDto;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationConfig.class)
public class ProducerRabbitTest extends TestConfigurationConfig {

    @InjectMocks
    private ProducerRabbit producerRabbit;

    @MockBean(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @MockBean(name = "rabbitTemplatePublish")
    private RabbitTemplate rabbitTemplatePublish;

    @MockBean(name = "rabbitTemplateOwner")
    private RabbitTemplate rabbitTemplateOwner;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private NewPublicationBrokerDto newPublicationBrokerDto;

    private NewCommentBrokerDto newCommentBrokerDto;

    private UserDto userDto;

    private NotificationFollowerBrokerDto notificationFollowerBrokerDto;
    private LikeRequestDto likeRequestDto;

    @BeforeEach
    public void setup() {

        userDto = userDto.builder()
                .userId(UUID.fromString("e1c96b65-773c-4593-809f-a8fc0348c0b0"))
                .firstName("Анатолий")
                .lastName("Петров")
                .build();


        newPublicationBrokerDto = newPublicationBrokerDto.builder()
                .publicationId(UUID.fromString("d1c0aa05-1848-4666-8474-288dae823d10"))
                .author(userDto)
                .build();

        newCommentBrokerDto = newCommentBrokerDto.builder()
                .commentId(UUID.fromString("d1c0aa05-1848-4666-8474-288dae823e22"))
                .authorComment(userDto)
                .publicationId(UUID.fromString("d1c0aa05-1848-4666-8474-288dae823d10"))
                .build();

        notificationFollowerBrokerDto = notificationFollowerBrokerDto.builder()
                .followerId(UUID.fromString("5f697ccd-d404-4c02-9ddb-af74761f52c0"))
                .author(userDto)
                .message("Новая публикация от " + userDto.getFirstName() + " " +
                        userDto.getLastName())
                .build();

        likeRequestDto = likeRequestDto.builder()
                .publicationId(UUID.fromString("d1c0aa05-1848-4666-8474-288dae823d10"))
                .author(userDto)
                .build();
    }


    @Test
    public void testSentMessageToNewPublicationQueue() throws JsonProcessingException {
        String message = jacksonObjectMapper.writeValueAsString(newPublicationBrokerDto);
        producerRabbit.sentMessageToNewPublicationQueue(newPublicationBrokerDto);

        verify(rabbitTemplatePublish, times(1))
                .convertAndSend(eq(RabbitMQConfig.NEW_PUBLICATIONS_QUEUE), eq("newPublication"), eq(message));
    }

    @Test
    public void testSentMessageNotificationNewPublication() throws JsonProcessingException {
        String message = jacksonObjectMapper.writeValueAsString(notificationFollowerBrokerDto);
        producerRabbit.sentMessageNotificationNewPublication(notificationFollowerBrokerDto);

        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.DIRECT_EXCHANGE_NAME), eq("newPublication"), eq(message));
    }

    @Test
    public void testSentMessageNotificationNewComment() throws JsonProcessingException {
        String message = jacksonObjectMapper.writeValueAsString(newCommentBrokerDto);
        producerRabbit.sentMessageNotificationNewComment(newCommentBrokerDto);

        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.DIRECT_EXCHANGE_NAME), eq("newComment"), eq(message));
    }

    @Test
    public void testSentMessageNotificationNewCommentOwner() throws JsonProcessingException {
        String message = jacksonObjectMapper.writeValueAsString(newCommentBrokerDto);
        producerRabbit.sentMessageNotificationNewCommentOwner(newCommentBrokerDto);

        verify(rabbitTemplateOwner, times(1))
                .convertAndSend(eq(RabbitMQConfig.DIRECT_EXCHANGE_OWNER), eq("newComment"), eq(message));
    }

    @Test
    public void testSentMessageNewLike() throws JsonProcessingException {
        String message = jacksonObjectMapper.writeValueAsString(likeRequestDto);
        producerRabbit.sentMessageNewLike(likeRequestDto);

        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.DIRECT_EXCHANGE_NAME), eq("newLike"), eq(message));
    }

    @Test
    public void testSentMessageNewLikeOwner() throws JsonProcessingException{
        String message = jacksonObjectMapper.writeValueAsString(likeRequestDto);
        producerRabbit.sentMessageNewLikeOwner(likeRequestDto);

        verify(rabbitTemplateOwner, times(1))
                .convertAndSend(eq(RabbitMQConfig.DIRECT_EXCHANGE_OWNER), eq("newLike"), eq(message));
    }
}

