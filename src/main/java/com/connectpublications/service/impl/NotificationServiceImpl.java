package com.connectpublications.service.impl;

import com.connectpublications.exception.PublicationNotFoundException;
import com.connectpublications.exception.UserNotFoundException;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.broker.NotificationFollowerBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.model.entity.Publication;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.PublicationRepository;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final String FOLLOWER_NOT_FOUND = "Follower not found with id: ";
    private static final String PUBLICATION_NOT_FOUND_MESSAGE = "Publication not found by id: ";
    private final PublicationRepository publicationRepository;

    @Value("${sender}")
    private String sender;

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public void handleNotificationNewPublication(NotificationFollowerBrokerDto notificationFollowerBrokerDto) {
        User follower = userRepository.findById(notificationFollowerBrokerDto.getFollowerId())
                .orElseThrow(() -> new UserNotFoundException(FOLLOWER_NOT_FOUND + notificationFollowerBrokerDto.getFollowerId()));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(follower.getEmail());
        message.setSubject("Новая публикация");
        message.setText(notificationFollowerBrokerDto.getMessage());
        mailSender.send(message);
        log.info("Notification of a new publication send to user {} {} by {}", follower.getFirstName(), follower.getLastName(), follower.getEmail());
    }

    @Override
    public void handleNotificationFollowersAddComment(NewCommentBrokerDto newCommentBrokerDto) {

        Publication publication = publicationRepository.findByIdWithAuthorAndFollowers(newCommentBrokerDto.getPublicationId())
                .orElseThrow(() -> new PublicationNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));

        List<User> followers = publication.getAuthor().getFollowers();

        for (User follower : followers) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(follower.getEmail());
            message.setSubject("Уведомление о новом комментарии");
            message.setText("У публикации пользователя " + newCommentBrokerDto.getAuthorComment().getFirstName() + " " +
                    newCommentBrokerDto.getAuthorComment().getLastName() + " появился новый комментарий");
            mailSender.send(message);
            log.info("Notification of a new comment on a subscriber's post sent to user {} {} by {}",
                    follower.getFirstName(), follower.getLastName(), follower.getEmail());
        }
    }

    @Override
    public void handleNotificationFollowersNewLike(LikeRequestDto likeRequestDto) {
        Publication publication = publicationRepository.findByIdWithAuthorAndFollowers(likeRequestDto.getPublicationId())
                .orElseThrow(() -> new PublicationNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));

        List<User> followers = publication.getAuthor().getFollowers();
        for (User follower : followers) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(follower.getEmail());
            message.setSubject("Уведомление о новом лайке");
            message.setText("У публикации пользователя " + publication.getAuthor().getFirstName() + " " +
                    publication.getAuthor().getLastName() + " появился новый лайк");
            mailSender.send(message);
            log.info("Notification subscriber about new like. Subscriber: {} {} {}",
                    follower.getFirstName(), follower.getLastName(), follower.getEmail());
        }

    }

}
