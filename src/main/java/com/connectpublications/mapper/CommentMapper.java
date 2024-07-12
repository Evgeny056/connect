package com.connectpublications.mapper;

import com.connectpublications.model.dto.broker.NewCommentBrokerDto;
import com.connectpublications.model.dto.request.CreateCommentRequestDto;
import com.connectpublications.model.dto.response.CommentResponseDto;
import com.connectpublications.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "publicationId", target = "publication.publicationId")
    @Mapping(source = "authorId", target = "authorComment.userId")
    @Mapping(source = "message", target = "text")
    Comment toEntity(CreateCommentRequestDto dto);


    @Mapping(source = "authorComment.userId", target = "author.userId")
    @Mapping(source = "authorComment.firstName", target = "author.firstName")
    @Mapping(source = "authorComment.lastName", target = "author.lastName")
    @Mapping(source = "commentId", target = "commentId")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "dateRecordComment", target = "date")
    CommentResponseDto toCommentResponseDto(Comment comment);


    @Mapping(source = "commentId", target = "commentId")
    @Mapping(source = "authorComment", target = "authorComment")
    @Mapping(source = "publication.publicationId", target = "publicationId")
    NewCommentBrokerDto toNewCommentBrokerDto(Comment comment);

}
