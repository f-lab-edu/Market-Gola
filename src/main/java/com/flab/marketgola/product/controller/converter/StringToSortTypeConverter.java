package com.flab.marketgola.product.controller.converter;

import com.flab.marketgola.product.domain.SortType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//SortType에 대한 요청으로 소문자가 왔을 때 대문자로 변경해서 매핑해주는 Converter
@Component
public class StringToSortTypeConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String source) {
        return source.isEmpty() ? null : SortType.valueOf(source.trim().toUpperCase());
    }
}
