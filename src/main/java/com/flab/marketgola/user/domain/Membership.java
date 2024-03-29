package com.flab.marketgola.user.domain;

import com.flab.marketgola.common.domain.BaseEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, unique = true, nullable = false)
    private String level;

    @Column(nullable = false)
    private BigDecimal pointRate;
}
