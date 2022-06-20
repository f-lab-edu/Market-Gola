package com.flab.marketgola.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.user.domain.Sample;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("메모리 DB에 저장 확인")
    void save() {
        //given
        Sample sample = new Sample();
        sample.setSampleName("sample");

        //when
        Sample savedSample = sampleRepository.save(sample);

        //then
        assertThat(sampleRepository.findById(savedSample.getId()).getSampleName())
                .isEqualTo("sample");
    }

}