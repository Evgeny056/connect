package com.connectpublications.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthorPublicationEmailDto {
    private UUID publicationId;
    private String emailAuthor;
}
