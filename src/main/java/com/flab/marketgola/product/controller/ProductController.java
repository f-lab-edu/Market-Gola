package com.flab.marketgola.product.controller;

import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.service.ProductService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(ProductController.BASE_PATH)
@RestController
public class ProductController {

    public static final String BASE_PATH = "/products";

    private final ProductService productService;

    @PostMapping
    ResponseEntity<DisplayProductResponseDto> createDisplayProduct(
            @RequestBody @Validated CreateDisplayProductRequestDto requestDto) {

        DisplayProductResponseDto responseDto = productService.createDisplayProductWithProducts(
                requestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(BASE_PATH + "/" + responseDto.getId()));

        return new ResponseEntity<>(responseDto, headers, HttpStatus.CREATED);
    }

}