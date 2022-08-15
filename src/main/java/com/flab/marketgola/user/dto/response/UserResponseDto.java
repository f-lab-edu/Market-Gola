package com.flab.marketgola.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String loginId;
    private String email;
    private String phoneNumber;
    private String name;


    @Builder
    public UserResponseDto(Long id, String loginId, String email, String phoneNumber, String name) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }
}
