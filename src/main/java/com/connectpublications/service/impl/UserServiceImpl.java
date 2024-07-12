package com.connectpublications.service.impl;

import com.connectpublications.exception.UserNotFoundException;
import com.connectpublications.mapper.UserMapper;
import com.connectpublications.model.dto.request.UserCreateRequestDto;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found by id: ";

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        return userRepository.save(userMapper.toUser(userCreateRequestDto));
    }

    @Override
    @Transactional
    public User findUser(UUID userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
    }
}
