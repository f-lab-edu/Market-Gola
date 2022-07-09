package com.flab.marketgola.sample.mapper;

import com.flab.marketgola.sample.domain.Sample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {

    void save(Sample sample);

    Sample findById(Long id);
}
