package com.flab.marketgola.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDto {

    @NotBlank(message = "아이디를 입력하지 않았습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하지 않았습니다.")
    private String password;

    @Builder
    public UserLoginDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
