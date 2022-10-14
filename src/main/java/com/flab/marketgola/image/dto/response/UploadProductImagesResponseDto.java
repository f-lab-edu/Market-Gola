package com.flab.marketgola.image.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadProductImagesResponseDto {

    String mainImageName;
    String descriptionImageName;

    public UploadProductImagesResponseDto(String mainImageName, String descriptionImageName) {
        this.mainImageName = mainImageName;
        this.descriptionImageName = descriptionImageName;
    }
}
