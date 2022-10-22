package com.flab.marketgola.image.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadProductImagesResponseDto {

    String mainImageUrl;
    String descriptionImageUrl;

    public UploadProductImagesResponseDto(String mainImageUrl, String descriptionImageUrl) {
        this.mainImageUrl = mainImageUrl;
        this.descriptionImageUrl = descriptionImageUrl;
    }
}
