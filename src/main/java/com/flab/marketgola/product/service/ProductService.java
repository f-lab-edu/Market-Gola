package com.flab.marketgola.product.service;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.exception.NoSuchCategoryException;
import com.flab.marketgola.product.exception.NoSuchProductException;
import com.flab.marketgola.product.repository.DisplayProductRepository;
import com.flab.marketgola.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final DisplayProductRepository displayProductRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public DisplayProductResponseDto getDisplayProductById(Long id) {
        DisplayProduct displayProduct = displayProductRepository.findByIdWithAll(id)
                .orElseThrow(NoSuchProductException::new);

        return DisplayProductResponseDto.of(displayProduct);
    }

    @Transactional
    public DisplayProductResponseDto createDisplayProductWithProducts(
            CreateDisplayProductRequestDto requestDto) {

        DisplayProduct displayProduct = requestDto.toDisplayProduct();
        List<Product> products = requestDto.toProductsWithDisplayProduct(displayProduct);
        displayProduct.calculatePrice(products);

        try {
            displayProductRepository.save(displayProduct);
        } catch (DataIntegrityViolationException e) {
            throw new NoSuchCategoryException(e);
        }

        productRepository.saveAll(products);

        return DisplayProductResponseDto.of(displayProduct);
    }

    @Transactional
    public DisplayProductResponseDto updateDisplayProductByIdWithProducts(
            UpdateDisplayProductWithProductsRequestDto requestDto) {

        DisplayProduct updateDisplayProduct = requestDto.toDisplayProduct();
        List<Product> products = requestDto.toProducts(updateDisplayProduct);
        updateDisplayProduct.calculatePrice(products);

        //DisplayProduct는 어차피 존재하는지 확인을 위해 Select 쿼리를 날려야 한다. save()를 하면 추가 쿼리를 날려야 하므로 변경 감지 방식을 사용
        DisplayProduct findDisplayProduct = displayProductRepository.findById(requestDto.getId())
                .orElseThrow(NoSuchProductException::new);
        findDisplayProduct.update(updateDisplayProduct);

        //Product가 존재하면 업데이트하고 없다면 새로 추가하는 방식으로 동작해야 함. 따라서 변경감지가 아닌 save로 저장. 내부의 merge 메서드를 이용
        products.forEach(productRepository::save);

        return DisplayProductResponseDto.of(findDisplayProduct);
    }

    public void deleteDisplayProductById(Long id) {
        displayProductRepository.findById(id).orElseThrow(NoSuchProductException::new);
        productRepository.updateStateByDisplayProductId(true, id);
    }

    @Transactional(readOnly = true)
    public DisplayProductListResponseDto getDisplayProductListByCategory(int categoryId,
            Pageable pageCondition) {

        Page<DisplayProduct> page = displayProductRepository.findByCategory(categoryId,
                pageCondition);

        return DisplayProductListResponseDto.of(page);
    }
}
