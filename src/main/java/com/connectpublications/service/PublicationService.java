package com.connectpublications.service;

import com.connectpublications.model.dto.CreateCommentRequestDto;
import com.connectpublications.model.dto.CreatePostRequestDto;
import com.connectpublications.model.entity.Comment;
import com.connectpublications.model.entity.Publication;

public interface PublicationService {

    Publication createPost(CreatePostRequestDto createPostRequestDto);
    Comment createComment(CreateCommentRequestDto createCommentRequestDto);
}
