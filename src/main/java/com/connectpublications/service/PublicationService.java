package com.connectpublications.service;

import com.connectpublications.model.dto.request.CreateCommentRequestDto;
import com.connectpublications.model.dto.request.CreatePostRequestDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.model.dto.response.CommentResponseDto;
import com.connectpublications.model.dto.response.PublicationResponseDto;

public interface PublicationService {

    PublicationResponseDto createPost(CreatePostRequestDto createPostRequestDto);
    CommentResponseDto createComment(CreateCommentRequestDto createCommentRequestDto);
    void addLikePublication(LikeRequestDto likeRequestDto);
}
