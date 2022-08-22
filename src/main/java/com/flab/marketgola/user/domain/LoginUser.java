package com.flab.marketgola.user.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginUser implements Serializable {

    private Long id;
    private String name;

    public LoginUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
