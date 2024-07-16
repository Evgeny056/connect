package com.connectpublications.messaging.consumer;

import com.connectpublications.config.TestConfigurationConfig;
import com.connectpublications.model.dto.UserDto;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
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
    }

    @Test
    public void testHandleNewPublicationQueue() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(newPublicationBrokerDto);

        consumerRabbit.handleNewPublicationQueue(message);

        verify(subscribeService, times(1)).handleNewPublication(any(NewPublicationBrokerDto.class));
    }

    @Test
    public void testHandleNewCommentFollower() throws Exception {
        String message = jacksonObjectMapper.writeValueAsString(newCommentBrokerDto);

        consumerRabbit.handleNewCommentFollower(message, "newComment");

        verify(notificationService, times(1)).handleNotificationFollowersAddComment(any(NewCommentBrokerDto.class));
    }
}
