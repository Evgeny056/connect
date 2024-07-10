package com.connectpublications.controller;

import com.connectpublications.model.dto.CreateCommentRequestDto;
import com.connectpublications.model.dto.CreatePostRequestDto;
import com.connectpublications.model.entity.Comment;
import com.connectpublications.model.entity.Publication;
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
    public ResponseEntity<Publication> createPost(@RequestBody CreatePostRequestDto createPostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.createPost(createPostRequestDto));

    }

    @PostMapping("/add-comment")
    public ResponseEntity<Comment> createComment(@RequestBody CreateCommentRequestDto createCommentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.createComment(createCommentRequestDto));
    }
}
