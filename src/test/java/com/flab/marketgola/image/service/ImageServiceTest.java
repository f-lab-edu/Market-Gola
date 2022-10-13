package com.flab.marketgola.image.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.services.s3.AmazonS3;
import com.flab.marketgola.image.constant.TestImageFactory;
import com.flab.marketgola.image.domain.MainImage;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("unit")
class ImageServiceTest {

    ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new S3ImageService(Mockito.mock(AmazonS3.class));
    }

    @DisplayName("이미지 저장 후 저장된 이미지의 이름을 반환한다.")
    @Test
    void upload() throws FileNotFoundException {
        //given
        MainImage mainImage = TestImageFactory.getMainImage();

        //when
        String storedName = imageService.upload(mainImage);

        //then
        Pattern pattern = Pattern.compile("[a-z0-9\\-]+\\.[a-zA-Z]+");
        boolean isMatch = pattern.matcher(storedName).matches();
        assertThat(isMatch).isTrue();
    }
}