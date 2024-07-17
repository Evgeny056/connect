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
    private UserDto subscriberUserDto;

    private NotificationFollowerBrokerDto notificationFollowerBrokerDto;
    private LikeRequestDto likeRequestDto;

    private static final String USER_DTO_ID = "bbc631ca-07ed-4450-a601-4657152e8ba3";
    private static final String USER_DTO_FIRST_NAME = "Иван";
    private static final String USER_DTO_LAST_NAME = "Матросов";

    private static final String USER_DTO_SUBSCRIBER_ID = "ea6d8948-59fe-4f2b-a0f7-79a22450c7ff";
    private static final String USER_DTO_SUBSCRIBER_FIRST_NAME = "Евгений";
    private static final String USER_DTO_SUBSCRIBER_LAST_NAME = "Шу";

    private static final String PUBLICATION_ID = "b0cc4ec5-3a7e-41bb-ba6c-5db19e27e143";
    private static final String COMMENT_ID = "bb0c3970-fc61-451a-a5dc-ffbda719b8fc";

    @BeforeEach
    public void setup() {

        jacksonObjectMapper = new ObjectMapper();

        userDto = new UserDto();
                userDto.setUserId((UUID.fromString(USER_DTO_ID)));
                userDto.setFirstName(USER_DTO_FIRST_NAME);
                userDto.setLastName(USER_DTO_LAST_NAME);

        subscriberUserDto = new UserDto();
                subscriberUserDto.setUserId(UUID.fromString(USER_DTO_SUBSCRIBER_ID));
                subscriberUserDto.setFirstName(USER_DTO_SUBSCRIBER_FIRST_NAME);
                subscriberUserDto.setLastName(USER_DTO_SUBSCRIBER_LAST_NAME);



        newPublicationBrokerDto = new NewPublicationBrokerDto();
                newPublicationBrokerDto.setPublicationId(UUID.fromString(PUBLICATION_ID));
                newPublicationBrokerDto.setAuthor(userDto);

        newCommentBrokerDto = new NewCommentBrokerDto();
                newCommentBrokerDto.setCommentId(UUID.fromString(COMMENT_ID));
                newCommentBrokerDto.setAuthorComment(subscriberUserDto);
                newCommentBrokerDto.setPublicationId(UUID.fromString(PUBLICATION_ID));

        notificationFollowerBrokerDto = new NotificationFollowerBrokerDto();
                notificationFollowerBrokerDto.setFollowerId(subscriberUserDto.getUserId());
                notificationFollowerBrokerDto.setAuthor(userDto);
                notificationFollowerBrokerDto.setMessage("Новая публикация от " + userDto.getFirstName() + " " +
                        userDto.getLastName());

        likeRequestDto = new LikeRequestDto();
                likeRequestDto.setPublicationId(UUID.fromString(PUBLICATION_ID));
                likeRequestDto.setAuthor(userDto);
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

