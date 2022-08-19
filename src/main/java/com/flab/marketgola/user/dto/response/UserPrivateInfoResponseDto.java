package com.flab.marketgola.user.dto.response;

import com.flab.marketgola.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrivateInfoResponseDto {

    private Long id;
    private String loginId;
    private String email;
    private String phoneNumber;
    private String name;

    public static UserPrivateInfoResponseDto of(User user) {
        return UserPrivateInfoResponseDto.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .build();
    }

    @Builder
    public UserPrivateInfoResponseDto(Long id, String loginId, String email, String phoneNumber,
            String name) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }
}
