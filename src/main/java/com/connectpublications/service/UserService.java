package com.connectpublications.service;

import com.connectpublications.model.dto.UserCreateRequestDto;
import com.connectpublications.model.entity.User;

public interface UserService {

    User createUser(UserCreateRequestDto userCreateRequestDto);
}
