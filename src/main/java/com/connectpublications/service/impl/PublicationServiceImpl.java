package com.connectpublications.service.impl;

import com.connectpublications.exception.PublicationNotFoundException;
import com.connectpublications.exception.UserNotFoundException;
import com.connectpublications.mapper.CommentMapper;
import com.connectpublications.mapper.PublicationMapper;
import com.connectpublications.model.dto.CreateCommentRequestDto;
import com.connectpublications.model.dto.CreatePostRequestDto;
import com.connectpublications.model.entity.Comment;
import com.connectpublications.model.entity.Publication;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.CommentRepository;
import com.connectpublications.repository.PublicationRepository;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Publication createPost(CreatePostRequestDto createPostRequestDto) {
        User authorPost = userRepository.findById(createPostRequestDto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE
                        + createPostRequestDto.getAuthorId()));

        Publication publicationResponse = publicationMapper.toEntity(createPostRequestDto);
        publicationResponse.setAuthor(authorPost);
        publicationRepository.save(publicationResponse);
        //sent rabbit
        return publicationResponse;
    }

    @Override
    public Comment createComment(CreateCommentRequestDto createCommentRequestDto) {

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
        //отправить в очередь
        return comment;
    }
}
