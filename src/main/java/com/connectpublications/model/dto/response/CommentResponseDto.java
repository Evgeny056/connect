package com.connectpublications.model.dto.response;

import com.connectpublications.model.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CommentResponseDto {

    private UUID commentId;
    private UserDto author;
    private String text;
    private Date date;

}
