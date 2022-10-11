package com.flab.marketgola.common.domain;

public enum ServiceType {
    USER("user"),PRODUCT("product");

    private String name;
    ServiceType(String name) {
        this.name = name;
    }
}
