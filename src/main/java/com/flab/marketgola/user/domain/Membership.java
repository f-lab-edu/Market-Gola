package com.flab.marketgola.user.domain;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Membership {

    private String level;
    private float pointRate;
}
