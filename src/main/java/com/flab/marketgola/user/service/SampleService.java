package com.flab.marketgola.user.service;

import com.flab.marketgola.user.domain.Sample;
import com.flab.marketgola.user.repository.SampleRepository;
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
