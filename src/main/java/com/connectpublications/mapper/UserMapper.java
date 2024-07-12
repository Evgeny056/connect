package com.connectpublications.mapper;

import com.connectpublications.model.dto.UserDto;
import com.connectpublications.model.dto.request.UserCreateRequestDto;
import com.connectpublications.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequestDto userCreateRequestDto);
}
