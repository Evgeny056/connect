package com.connectpublications.controller;

import com.connectpublications.model.dto.request.UserCreateRequestDto;
import com.connectpublications.model.entity.User;
import com.connectpublications.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateRequestDto));
    }

    @GetMapping("/user-info")
    public ResponseEntity<User> getUser(@RequestParam UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(userId));
    }
}
