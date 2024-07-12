package com.connectpublications.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubscribeRequestDto {
    private UUID userId;
    private UUID followerId;
}
