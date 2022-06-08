package com.flab.marketgola.user.domain;

import lombok.Data;

@Data
public class Sample {
    private Long id;
    private String sampleName;

    public Sample() {}

    public Sample(Long id, String sampleName) {
        this.id = id;
        this.sampleName = sampleName;
    }
}
