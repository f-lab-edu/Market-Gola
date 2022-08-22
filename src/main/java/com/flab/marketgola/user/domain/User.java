package com.flab.marketgola.user.domain;

import com.flab.marketgola.user.util.PasswordEncryptionUtil;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class User {

    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private LocalDate birth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String loginId, String password, String name, String email,
            String phoneNumber,
            Gender gender, LocalDate birth, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birth = birth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void encryptPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.password = PasswordEncryptionUtil.encrypt(password);
    }

    public boolean isPasswordEqual(String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        return PasswordEncryptionUtil.validatePassword(this.password, password);
    }
}
