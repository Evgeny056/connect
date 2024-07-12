package com.connectpublications.model.dto.response;

import com.connectpublications.model.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PublicationResponseDto {

    private UUID publicationId;
    private UserDto author;
    private String message;
    private Date dateRecord;
}
