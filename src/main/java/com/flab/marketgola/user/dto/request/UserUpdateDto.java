package com.flab.marketgola.user.dto.request;

import com.flab.marketgola.user.domain.Gender;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserUpdateDto {

    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate birth;
    private Gender gender;
}
