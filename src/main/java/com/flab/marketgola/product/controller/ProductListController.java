package com.flab.marketgola.product.controller;


import com.flab.marketgola.product.dto.request.GetDisplayProductsCondition;
import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto;
import com.flab.marketgola.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductListController {

    private final ProductService productService;

    @GetMapping("/categories/{categoryId}/products")
    ResponseEntity<DisplayProductListResponseDto> getDisplayProductsByCategory(
            @PathVariable int categoryId,
            @ModelAttribute @Validated GetDisplayProductsCondition condition) {

        return new ResponseEntity(
                productService.getDisplayProductListByCategory(categoryId, condition),
                HttpStatus.OK);
    }
}
