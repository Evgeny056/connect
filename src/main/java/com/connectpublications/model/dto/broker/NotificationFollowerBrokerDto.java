package com.connectpublications.model.dto.broker;

import com.connectpublications.model.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class NotificationFollowerBrokerDto {

    private UUID followerId;
    private UserDto author;
    private String message;

}
