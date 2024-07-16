package com.connectpublications.model.dto.request;

import com.connectpublications.model.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@Builder
public class LikeRequestDto {
    private UUID publicationId;
    private UserDto author;
}
