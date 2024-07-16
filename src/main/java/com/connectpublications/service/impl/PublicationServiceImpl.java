package com.connectpublications.service.impl;

import com.connectpublications.exception.PublicationNotFoundException;
import com.connectpublications.exception.UserNotFoundException;
import com.connectpublications.mapper.CommentMapper;
import com.connectpublications.mapper.PublicationMapper;
import com.connectpublications.messaging.producer.MessageProducer;
import com.connectpublications.model.dto.request.CreateCommentRequestDto;
import com.connectpublications.model.dto.request.CreatePostRequestDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.model.dto.response.CommentResponseDto;
import com.connectpublications.model.dto.response.PublicationResponseDto;
import com.connectpublications.model.entity.Comment;
import com.connectpublications.model.entity.Publication;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.CommentRepository;
import com.connectpublications.repository.PublicationRepository;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found by id: ";
    private static final String PUBLICATION_NOT_FOUND_MESSAGE = "Publication not found by id: ";

    private final PublicationMapper publicationMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;
    private final CommentMapper commentMapper;
    private final MessageProducer messageProducer;

    @Override
    @Transactional
    public PublicationResponseDto createPost(CreatePostRequestDto createPostRequestDto) {
        User authorPost = userRepository.findById(createPostRequestDto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE
                        + createPostRequestDto.getAuthorId()));

        Publication publication = publicationMapper.toEntity(createPostRequestDto);
        publication.setAuthor(authorPost);
        publicationRepository.save(publication);
        messageProducer.sentMessageToNewPublicationQueue(publicationMapper.toPublicationBrokerDto(publication));
        return publicationMapper.toPublicationResponseDto(publication);
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(CreateCommentRequestDto createCommentRequestDto) {
        User author = userRepository.findById(createCommentRequestDto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE
                        + createCommentRequestDto.getAuthorId()));

        Publication publication = publicationRepository.findById(createCommentRequestDto.getPublicationId())
                .orElseThrow(() -> new PublicationNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE
                        + createCommentRequestDto.getPublicationId()));

        Comment comment = commentMapper.toEntity(createCommentRequestDto);
        comment.setAuthorComment(author);
        comment.setPublication(publication);
        commentRepository.save(comment);

        messageProducer.sentMessageNotificationNewComment(commentMapper.toNewCommentBrokerDto(comment));
        messageProducer.sentMessageNotificationNewCommentOwner(commentMapper.toNewCommentBrokerDto(comment));

        return commentMapper.toCommentResponseDto(comment);
    }


    @Transactional
    public void addLikePublication(LikeRequestDto likeRequestDto) {
        Publication publication = publicationRepository.findById(likeRequestDto.getPublicationId())
                .orElseThrow(() -> new PublicationNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));

        int countLike = publication.getCountLike();
        publication.setCountLike(countLike + 1);
        publicationRepository.save(publication);

        messageProducer.sentMessageNewLike(likeRequestDto);
        messageProducer.sentMessageNewLikeOwner(likeRequestDto);
    }
}
