package com.flab.marketgola.sample.controller;

import com.flab.marketgola.sample.domain.Sample;
import com.flab.marketgola.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/save")
    public String saveSample(@RequestParam String sampleName) {
        Sample sample = new Sample();
        sample.setSampleName(sampleName);
        sampleService.save(sample);
        return "ok";
    }

}
