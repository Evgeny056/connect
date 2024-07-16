package com.connectpublications.messaging.consumer;

import com.connectpublications.config.TestConfigurationConfig;
import com.connectpublications.model.dto.UserDto;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.service.ActivityService;
import com.connectpublications.service.NotificationService;
import com.connectpublications.service.SubscribeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationConfig.class)
public class ConsumerRabbitTest extends TestConfigurationConfig {


    @InjectMocks
    private ConsumerRabbit consumerRabbit;

    @MockBean
    private SubscribeService subscribeService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private UserDto userDto;

    private NewPublicationBrokerDto newPublicationBrokerDto;

    private NewCommentBrokerDto newCommentBrokerDto;

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
    public void testHandleNewPublicationQueue() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(newPublicationBrokerDto);

        consumerRabbit.handleNewPublicationQueue(message);

        verify(subscribeService, times(1)).handleNewPublication(any(NewPublicationBrokerDto.class));
    }

    @Test
    public void testHandleNotificationFollowerPublication() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(newPublicationBrokerDto);
        consumerRabbit.handleNotificationFollower(message, "newPublication");

        verify(notificationService, times(1))
                .handleNotificationNewPublication(any(NotificationFollowerBrokerDto.class));
    }

    @Test
    public void testHandleNotificationFollowerNewComment() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(newCommentBrokerDto);
        consumerRabbit.handleNotificationFollower(message, "newComment");

        verify(notificationService, times(1))
                .handleNotificationFollowersAddComment(any(NewCommentBrokerDto.class));
    }

    @Test
    public void testHandleNotificationFollowerLike() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(likeRequestDto);
        consumerRabbit.handleNotificationFollower(message, "newLike");

        verify(notificationService, times(1))
                .handleNotificationFollowersNewLike(any(LikeRequestDto.class));
    }

    @Test
    public void testOwnerAlertsNewComment() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(newCommentBrokerDto);
        consumerRabbit.ownerAlerts(message, "newComment");

        verify(activityService, times(1))
                .notificationOwnerNewComment(any(NewCommentBrokerDto.class));

    }

    @Test
    public void testOwnerAlertsNewLike() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(likeRequestDto);
        consumerRabbit.ownerAlerts(message, "newLike");

        verify(activityService, times(1))
                .notificationOwnerNewLike(any(LikeRequestDto.class));
    }

}
