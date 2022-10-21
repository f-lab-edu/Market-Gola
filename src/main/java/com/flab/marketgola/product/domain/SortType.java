package com.flab.marketgola.product.domain;

import lombok.Getter;

@Getter
public enum SortType {
    PRICE_ASC("price asc"), PRICE_DESC("price desc"), DATE_DESC("created_at desc");

    private String sqlValue;

    SortType(String sqlValue) {
        this.sqlValue = sqlValue;
    }
}
