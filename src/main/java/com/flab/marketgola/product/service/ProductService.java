package com.flab.marketgola.product.service;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.exception.NoSuchCategoryException;
import com.flab.marketgola.product.exception.NoSuchProductException;
import com.flab.marketgola.product.mapper.DisplayProductMapper;
import com.flab.marketgola.product.mapper.ProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final DisplayProductMapper displayProductRepository;
    private final ProductMapper productRepository;

    public DisplayProductResponseDto getDisplayProductById(Long id) {
        DisplayProduct displayProduct = displayProductRepository.findById(id)
                .orElseThrow(() -> new NoSuchProductException());

        return DisplayProductResponseDto.of(displayProduct);
    }

    @Transactional
    public DisplayProductResponseDto createDisplayProductWithProducts(
            CreateDisplayProductRequestDto requestDto) {

        DisplayProduct displayProduct = requestDto.toDisplayProduct();
        try {
            displayProductRepository.insert(displayProduct);
        } catch (DataIntegrityViolationException e) {
            throw new NoSuchCategoryException(e);
        }

        List<Product> products = requestDto.toProductsWithDisplayProduct(displayProduct);
        products.forEach(product -> productRepository.insert(product));

        return getDisplayProductById(displayProduct.getId());
    }

}
