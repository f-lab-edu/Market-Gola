package com.flab.marketgola.image.controller;

import com.flab.marketgola.image.domain.DescriptionImage;
import com.flab.marketgola.image.domain.MainImage;
import com.flab.marketgola.image.dto.response.UploadProductImagesResponseDto;
import com.flab.marketgola.image.service.ImageService;
import com.flab.marketgola.product.controller.ProductController;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping(ImageController.BASE_PATH)
@RestController
public class ImageController {

    public static final String BASE_PATH = "/images";
    private final ImageService imageService;

    @PostMapping(ProductController.BASE_PATH)
    public ResponseEntity<UploadProductImagesResponseDto> uploadProductImages(
            @RequestPart MultipartFile mainImage,
            @RequestPart MultipartFile descriptionImage) throws IOException {

        String mainImageStoredName = imageService.upload(
                new MainImage(mainImage.getInputStream(), mainImage.getOriginalFilename()));

        String descriptionImageStoredName = imageService.upload(
                new DescriptionImage(descriptionImage.getInputStream(),
                        descriptionImage.getOriginalFilename()));

        return new ResponseEntity<>(
                new UploadProductImagesResponseDto(mainImageStoredName, descriptionImageStoredName),
                HttpStatus.OK);
    }
}
