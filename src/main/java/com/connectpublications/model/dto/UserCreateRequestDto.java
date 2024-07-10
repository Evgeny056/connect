package com.connectpublications.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {

    private String firstName;
    private String lastName;
    private String mobilePhoneNumber;
    private String email;
}
