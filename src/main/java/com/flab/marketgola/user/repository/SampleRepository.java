package com.flab.marketgola.user.repository;

import com.flab.marketgola.user.domain.Sample;
import com.flab.marketgola.user.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SampleRepository {

    private final SampleMapper sampleMapper;

    public Sample save(Sample sample) {
        sampleMapper.save(sample);
        return sample;
    }

    public Sample findById(Long id) {
        return sampleMapper.findById(id);
    }
}
