package com.connectpublications.controller;

import com.connectpublications.model.dto.response.MessageResponse;
import com.connectpublications.model.dto.request.SubscribeRequestDto;
import com.connectpublications.service.CreateSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscribeController {

    private final CreateSubscribeService createSubscribeService;

    @PutMapping("/follow-user-updates")
    public ResponseEntity<MessageResponse> getFollowing(@RequestBody SubscribeRequestDto subscribeRequestDto) {
        createSubscribeService.followUser(subscribeRequestDto);
        return ResponseEntity.ok(new MessageResponse("User subscribed to usage updates"));
    }
}
