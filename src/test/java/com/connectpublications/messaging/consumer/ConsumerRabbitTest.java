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
