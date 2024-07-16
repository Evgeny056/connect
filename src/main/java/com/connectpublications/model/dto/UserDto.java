package com.connectpublications.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class UserDto {

    private UUID userId;
    private String firstName;
    private String lastName;
}
