package com.flab.marketgola.user.domain;

public enum Gender {

    MALE("male"), FEMALE("female");

    private final String value;

    Gender(String value) {
        this.value = value;
    }
}
