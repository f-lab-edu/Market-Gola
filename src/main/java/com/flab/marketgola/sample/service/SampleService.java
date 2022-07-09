package com.flab.marketgola.sample.service;

import com.flab.marketgola.sample.domain.Sample;
import com.flab.marketgola.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public void save(Sample sample) {
        sampleRepository.save(sample);
    }
}
