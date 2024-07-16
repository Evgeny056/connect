package com.connectpublications.model.dto.broker;

import com.connectpublications.model.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class NotificationFollowerBrokerDto {

    private UUID followerId;
    private UserDto author;
    private String message;

}
