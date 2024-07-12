package com.connectpublications.service;

import com.connectpublications.model.dto.request.UserCreateRequestDto;
import com.connectpublications.model.entity.User;

import java.util.UUID;

public interface UserService {
    User createUser(UserCreateRequestDto userCreateRequestDto);
    User findUser(UUID userId);
}
