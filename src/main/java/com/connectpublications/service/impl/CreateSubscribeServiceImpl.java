package com.connectpublications.service.impl;

import com.connectpublications.exception.FollowerExistException;
import com.connectpublications.exception.UserNotFoundException;
import com.connectpublications.model.dto.request.SubscribeRequestDto;
import com.connectpublications.model.entity.User;
import com.connectpublications.repository.UserRepository;
import com.connectpublications.service.CreateSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CreateSubscribeServiceImpl implements CreateSubscribeService {

    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found by id: ";
    private static final String FOLLOWER_NOT_FOUND_MESSAGE = "Follower not found by id: ";
    private static final String FOLLOWER_EXIST = "Subscription completed previously";

    @Override
    @Transactional
    public void followUser(SubscribeRequestDto subscribeRequestDto) {

        User user = userRepository.findById(subscribeRequestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + subscribeRequestDto.getUserId()));

        User follower = userRepository.findById(subscribeRequestDto.getFollowerId())
                .orElseThrow(() -> new UserNotFoundException(FOLLOWER_NOT_FOUND_MESSAGE + subscribeRequestDto.getFollowerId()));

        if (user.getFollowers().contains(follower)) {
            throw new FollowerExistException(FOLLOWER_EXIST);
        }
        user.getFollowers().add(follower);

        if (!follower.getFollowers().contains(user)) {
            follower.getFollowing().add(user);
        }

        userRepository.save(user);
        userRepository.save(follower);
    }

    public void unfollowUser(SubscribeRequestDto subscribeRequestDto) {
        User user = userRepository.findById(subscribeRequestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + subscribeRequestDto.getUserId()));

        User follower = userRepository.findById(subscribeRequestDto.getFollowerId())
                .orElseThrow(() -> new UserNotFoundException(FOLLOWER_NOT_FOUND_MESSAGE + subscribeRequestDto.getFollowerId()));

        user.getFollowers().remove(follower);
        follower.getFollowing().remove(user);

        userRepository.save(user);
        userRepository.save(follower);
    }
}
