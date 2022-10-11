package com.flab.marketgola.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginUser {

    private Long id;
    private String name;

    public LoginUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
