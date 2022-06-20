package com.flab.marketgola.user.mapper;

import com.flab.marketgola.user.domain.Sample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {

    void save(Sample sample);

    Sample findById(Long id);
}
