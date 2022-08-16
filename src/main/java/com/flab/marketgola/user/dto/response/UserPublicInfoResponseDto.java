package com.flab.marketgola.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPublicInfoResponseDto {

    private String loginId;
    private String name;

    public UserPublicInfoResponseDto(String loginId, String name) {
        this.loginId = loginId;
        this.name = name;
    }
}
