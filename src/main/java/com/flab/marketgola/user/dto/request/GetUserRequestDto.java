package com.flab.marketgola.user.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetUserRequestDto {

    private String loginId;
    private String email;
    private String phoneNumber;


    @Builder
    public GetUserRequestDto(String loginId, String email, String phoneNumber) {
        this.loginId = loginId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}

