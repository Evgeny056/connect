package com.connectpublications.mapper;

import com.connectpublications.model.dto.CreateCommentRequestDto;
import com.connectpublications.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "publicationId", target = "publication.publicationId")
    @Mapping(source = "authorId", target = "authorComment.userId")
    @Mapping(source = "message", target = "text")
    Comment toEntity(CreateCommentRequestDto dto);
}
