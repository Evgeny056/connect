package com.connectpublications.mapper;

import com.connectpublications.model.dto.broker.NewPublicationBrokerDto;
import com.connectpublications.model.dto.request.CreatePostRequestDto;
import com.connectpublications.model.dto.response.PublicationResponseDto;
import com.connectpublications.model.entity.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicationMapper {

  @Mapping(source = "authorId", target = "author.userId")
  Publication toEntity(CreatePostRequestDto createPostRequestDto);

  @Mapping(source = "author.userId", target = "author.userId")
  @Mapping(source = "author.firstName", target = "author.firstName")
  @Mapping(source = "author.lastName", target = "author.lastName")
  @Mapping(source = "publicationId", target = "publicationId")
  @Mapping(source = "message", target = "message")
  @Mapping(source = "dateRecord", target = "dateRecord")
  PublicationResponseDto toPublicationResponseDto(Publication publication);

  @Mapping(source = "author.userId", target = "author.userId")
  @Mapping(source = "author.firstName", target = "author.firstName")
  @Mapping(source = "author.lastName", target = "author.lastName")
  @Mapping(source = "publicationId", target = "publicationId")
  NewPublicationBrokerDto toPublicationBrokerDto(Publication publication);



}
