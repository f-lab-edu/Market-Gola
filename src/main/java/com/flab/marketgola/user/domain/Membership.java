package com.flab.marketgola.user.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Membership {

    private String level;
    private BigDecimal pointRate;
}
