package com.flab.marketgola.product.controller;


import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto;
import com.flab.marketgola.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductListController {

    private final ProductService productService;

    @GetMapping("/categories/{categoryId}/products")
    ResponseEntity<DisplayProductListResponseDto> getDisplayProductsByCategory(
            @PathVariable int categoryId, Pageable pageCondition) {

        return new ResponseEntity<>(
                productService.getDisplayProductListByCategory(categoryId, pageCondition),
                HttpStatus.OK);
    }
}
