package com.flab.marketgola.image.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.image.constant.TestImageFactory;
import com.flab.marketgola.image.domain.DescriptionImage;
import com.flab.marketgola.image.domain.MainImage;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("unit")
@SpringBootTest(classes = TestRedisConfiguration.class)
class ImageServiceTest {

    @Autowired
    ImageService imageService;

    @DisplayName("이미지 저장 후 저장된 이미지의 URL을 반환한다.")
    @Test
    void upload() throws FileNotFoundException {
        //given
        MainImage mainImage = TestImageFactory.getMainImage();
        DescriptionImage descriptionImage = TestImageFactory.getDescriptionImage();
        String domain = "https://www.image.gola.com/products";

        //when
        String mainImageUrl = imageService.upload(mainImage);
        String descriptionImageUrl = imageService.upload(descriptionImage);

        //then
        assertThat(mainImageUrl).contains(domain);
        assertThat(descriptionImageUrl).contains(domain);
    }
}