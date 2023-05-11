package com.flab.marketgola.user.domain;

import com.flab.marketgola.common.domain.BaseEntity;
import com.flab.marketgola.user.util.PasswordEncryptionUtil;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "`user`")
@Getter
@ToString
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true, nullable = false)
    private String loginId;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, unique = true, nullable = false)
    private String email;

    @Column(length = 11, unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    @Builder
    public User(Long id, String loginId, String password, String name, String email,
            String phoneNumber, Gender gender, LocalDate birth) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birth = birth;
    }

    public void encryptPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.password = PasswordEncryptionUtil.encrypt(password);
    }

    public boolean isPasswordEqual(String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        return PasswordEncryptionUtil.validatePassword(this.password, password);
    }

    public void addShippingAddress(ShippingAddress shippingAddress) {
        shippingAddresses.add(shippingAddress);
        shippingAddress.setUser(this);
    }
}
