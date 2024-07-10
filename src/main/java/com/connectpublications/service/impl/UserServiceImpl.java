package com.connectpublications.service.impl;

import com.connectpublications.mapper.UserMapper;
import com.connectpublications.model.dto.UserCreateRequestDto;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        return userRepository.save(userMapper.toUser(userCreateRequestDto));
    }
}
