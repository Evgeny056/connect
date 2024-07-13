package com.connectpublications.mapper;

import com.connectpublications.model.dto.request.UserCreateRequestDto;
import com.connectpublications.model.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequestDto userCreateRequestDto);
}
