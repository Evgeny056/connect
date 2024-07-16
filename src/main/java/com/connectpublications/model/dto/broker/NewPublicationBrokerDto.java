package com.connectpublications.model.dto.broker;

import com.connectpublications.model.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.UUID;

@Getter
@Setter
@Builder
public class NewPublicationBrokerDto {

    private UUID publicationId;
    private UserDto author;

    @Override
    public String toString() {
        return "publicationId= " + publicationId +
                ", author=" + author.getFirstName() + " " + author.getLastName()
                ;
    }
}
