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
public class NewCommentBrokerDto {

    private UUID commentId;
    private UserDto authorComment;
    private UUID publicationId;
}
