package com.connectpublications.model.dto.request;

import com.connectpublications.model.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class LikeRequestDto {
    private UUID publicationId;
    private UserDto author;
}
