package com.sparta.springweekassignment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
public class SignupRequestDto {
    @NotNull(message = "Username cannot be null")
    @Size(min = 4, max = 10, message = "username은 4자이상 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "Username은 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 15, message = "password는 최소 4자 이상, 10자 이하여야합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password는 알파벳 대소문자, 숫자로 구성되어야 합니다.")
    private String password;

}

