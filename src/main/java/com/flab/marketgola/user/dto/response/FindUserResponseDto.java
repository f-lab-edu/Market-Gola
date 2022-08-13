package com.flab.marketgola.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserResponseDto {

    private Long id;

    public FindUserResponseDto(Long id) {
        this.id = id;
    }
}
