package com.marketgola.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String loginId;
    private String email;

    public User(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }
}
