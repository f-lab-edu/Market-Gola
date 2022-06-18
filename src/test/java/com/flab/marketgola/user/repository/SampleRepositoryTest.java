package com.flab.marketgola.user.repository;

import com.flab.marketgola.user.domain.Sample;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@RequiredArgsConstructor
class SampleRepositoryTest {

    @Autowired
    public SampleRepository sampleRepository;

    @Test
    void save_메모리DB에_저장_확인() {
        //given
        Sample sample = new Sample();
        sample.setSampleName("sample");

        //when
        sampleRepository.save(sample);

        //then
        Assertions.assertThat(sampleRepository.findById(1L).getSampleName()).isEqualTo("sample");
    }

}