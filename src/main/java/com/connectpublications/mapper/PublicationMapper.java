package com.connectpublications.mapper;

import com.connectpublications.model.dto.CreatePostRequestDto;
import com.connectpublications.model.entity.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicationMapper {

  @Mapping(source = "authorId", target = "author.userId")
  Publication toEntity(CreatePostRequestDto createPostRequestDto);

}
