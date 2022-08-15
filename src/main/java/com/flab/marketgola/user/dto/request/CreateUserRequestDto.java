package com.flab.marketgola.user.dto.request;

import com.flab.marketgola.user.domain.Gender;
import com.flab.marketgola.user.domain.ShippingAddress;
import com.flab.marketgola.user.domain.User;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserRequestDto {

    @Pattern(regexp = "[A-Za-z]+[A-Za-z0-9]{5,15}", message = "6자 이상 16자 이하의 영문 또는 영문 숫자 조합만 가능합니다.")

    @NotBlank(message = "아이디를 입력하지 않았습니다.")
    private String loginId;
    @Pattern(regexp = "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{10,64}$", message = "10자 이상 64자 이하의 영문/숫자/특수문자(공백 제외)만 허용하며, 2개 이상 조합해주세요.")
    @NotBlank(message = "비밀번호를 입력하지 않았습니다.")
    private String password;
    @NotBlank(message = "이름을 입력하지 않았습니다.")
    private String name;
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력하지 않았습니다.")
    private String email;
    @Pattern(regexp = "[0-9]{10,11}", message = "전화번호 형식에 맞지 않습니다.")
    @NotBlank(message = "전화번호를 입력하지 않았습니다.")
    private String phoneNumber;
    private Gender gender;
    private LocalDate birth;

    @NotBlank(message = "주소를 입력하지 않았습니다.")
    private String address;

    @Builder
    public CreateUserRequestDto(String loginId, String password, String name, String email,
            String phoneNumber,
            Gender gender, LocalDate birth, String address) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birth = birth;
        this.address = address;
    }

    public User toUser() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birth(birth)
                .build();
    }

    public ShippingAddress toShippingAddress(User user) {
        return ShippingAddress.builder()
                .user(user)
                .address(address)
                .isDefault(true)
                .build();
    }
}
