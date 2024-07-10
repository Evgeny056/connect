package com.connectpublications.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePostRequestDto {

    private UUID authorId;
    private String message;

}
