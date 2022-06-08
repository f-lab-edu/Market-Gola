package com.flab.marketgola.user.repository;

import com.flab.marketgola.user.domain.Sample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SampleRepository {

    void save(Sample sample);
}
