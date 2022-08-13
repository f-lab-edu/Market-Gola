package com.flab.marketgola.user.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindUserRequestDto {

    private String loginId;
    private String email;
    private String phoneNumber;


    @Builder
    public FindUserRequestDto(String loginId, String email, String phoneNumber) {
        this.loginId = loginId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}

