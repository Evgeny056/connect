package com.connectpublications.service.impl;

import com.connectpublications.exception.LoadingDataException;
import com.connectpublications.model.dto.AuthorPublicationEmailDto;
import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.repository.PublicationRepository;
import com.connectpublications.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private static final String LOADING_DATA_BED = "Failed to load date";

    @Value("${sender}")
    private String sender;

    private final PublicationRepository publicationRepository;
    private final JavaMailSender mailSender;

    @Override
    public void notificationOwnerNewComment(NewCommentBrokerDto newCommentBrokerDto) {
        AuthorPublicationEmailDto authorPublicationEmailDto = publicationRepository
                .findPublicationAuthorEmail(newCommentBrokerDto.getPublicationId())
                .orElseThrow(()->new LoadingDataException(LOADING_DATA_BED));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(authorPublicationEmailDto.getEmailAuthor());
        message.setSubject("Новый комментарий");
        message.setText(String.format("У вашей публикации появился новый комментарий от %s %s",
                newCommentBrokerDto.getAuthorComment().getFirstName(), newCommentBrokerDto.getAuthorComment().getLastName()));
        mailSender.send(message);
        log.info("Notification of a new comment {} {} by {}", newCommentBrokerDto.getAuthorComment().getFirstName(),
                newCommentBrokerDto.getAuthorComment().getLastName(), authorPublicationEmailDto.getEmailAuthor());

    }

    @Override
    @Transactional
    public void notificationOwnerNewLike(LikeRequestDto likeRequestDto) {
        AuthorPublicationEmailDto publicationAuthorEmail = publicationRepository.findPublicationAuthorEmail(likeRequestDto.getPublicationId())
                .orElseThrow(()-> new LoadingDataException(LOADING_DATA_BED));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(publicationAuthorEmail.getEmailAuthor());
        message.setSubject("Новый лайк");
        message.setText(String.format("Вашей публикации поставил(а) лайк %s %s",
                likeRequestDto.getAuthor().getFirstName(), likeRequestDto.getAuthor().getLastName()));
        mailSender.send(message);
        log.info("Notification of a new like {} {} by {}", likeRequestDto.getAuthor().getFirstName(),
                likeRequestDto.getAuthor().getLastName(),publicationAuthorEmail.getEmailAuthor());
    }
}
