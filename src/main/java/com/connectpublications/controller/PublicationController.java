package com.connectpublications.controller;

import com.connectpublications.model.dto.request.CreateCommentRequestDto;
import com.connectpublications.model.dto.request.CreatePostRequestDto;
import com.connectpublications.model.dto.request.LikeRequestDto;
import com.connectpublications.model.dto.response.CommentResponseDto;
import com.connectpublications.model.dto.response.MessageResponse;
import com.connectpublications.model.dto.response.PublicationResponseDto;
import com.connectpublications.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publication")
public class PublicationController {

    private final PublicationService publicationService;

    @PostMapping("/new-post")
    public ResponseEntity<PublicationResponseDto> createPost(@RequestBody CreatePostRequestDto createPostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.createPost(createPostRequestDto));

    }

    @PostMapping("/add-comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CreateCommentRequestDto createCommentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.createComment(createCommentRequestDto));
    }

    @PostMapping("/like")
    public ResponseEntity<MessageResponse> addLikePublication(@RequestBody LikeRequestDto likeRequestDto) {
        publicationService.addLikePublication(likeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Successfully added like to publication"));
    }
}
