package com.flab.marketgola.product.service;

import static com.flab.marketgola.product.constant.TestDisplayProductFactory.*;
import static com.flab.marketgola.product.constant.TestProductFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.dto.response.ProductResponseDto;
import com.flab.marketgola.product.exception.NoSuchCategoryException;
import com.flab.marketgola.product.exception.NoSuchProductException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("unit")
@SpringBootTest(classes = TestRedisConfiguration.class)
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @DisplayName("전시용 상품 저장시 정상적으로 전시용 상품 정보를 저장해야 한다.")
    @Test
    void createDisplayProduct() {
        //given
        CreateDisplayProductRequestDto requestDto = generateCreateDisplayProductRequestDto(
                List.of(generateCreateProductRequestDto()));

        //when
        DisplayProductResponseDto responseDto = productService.createDisplayProductWithProducts(
                requestDto);

        //then
        assertThat(productService.getDisplayProductById(responseDto.getId())).isNotNull();
    }


    @DisplayName("전시용 상품 저장시 관련된 상품 정보들 또한 같이 저장된다.")
    @Test
    void createDisplayProduct_insert_products() {
        //given
        CreateDisplayProductRequestDto requestDto = generateCreateDisplayProductRequestDto(
                List.of(generateCreateProductRequestDto()));

        //when
        DisplayProductResponseDto responseDto = productService.createDisplayProductWithProducts(
                requestDto);

        //then
        List<ProductResponseDto> products = productService.getDisplayProductById(
                responseDto.getId()).getProducts();

        assertThat(products).isNotEmpty();
    }

    @DisplayName("존재하지 않는 카테고리를 가진 전시용 상퓸은 저장할 수 없다.")
    @Test
    void createDisplayProduct_need_right_category() {
        //given
        CreateDisplayProductRequestDto requestDto = generateCreateDisplayProductRequestDto(
                List.of(generateCreateProductRequestDto()));

        int notExistCategory = 100;
        requestDto.setProductCategoryId(notExistCategory);

        //then
        assertThatThrownBy(() -> productService.createDisplayProductWithProducts(requestDto))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("정상적으로 전시용 상품을 찾을 수 있다.")
    @Test
    void getDisplayProductById() {
        //when
        DisplayProductResponseDto responseDto = productService.getDisplayProductById(
                PRE_INSERTED_DISPLAY_PRODUCT_ID);

        //then
        assertThat(responseDto.getId()).isNotNull();
        assertThat(responseDto.getName()).isNotNull();
        assertThat(responseDto.getProducts()).isNotEmpty();
    }

    @DisplayName("전시용 상품의 가격은 관련된 상품 가격 중 최소값이다.")
    @Test
    void getDisplayProductById_price() {
        //when
        DisplayProductResponseDto responseDto = productService.getDisplayProductById(
                PRE_INSERTED_DISPLAY_PRODUCT_ID);

        //then
        assertThat(responseDto.getPrice()).isEqualTo(1000);
    }

    @DisplayName("전시용 상품과 관련된 실제 상품은 함께 수정 된다.")
    @Test
    void updateDisplayProductByIdWithProducts() {
        //given
        UpdateProductRequestDto updateProductRequestDto1 = generateUpdateProductRequestDto();
        updateProductRequestDto1.setId(PRE_INSERTED_PRODUCT_ID_1);
        updateProductRequestDto1.setName("업데이트 상품 이름1");

        UpdateProductRequestDto updateProductRequestDto2 = generateUpdateProductRequestDto();
        updateProductRequestDto2.setId(PRE_INSERTED_PRODUCT_ID_2);
        updateProductRequestDto2.setName("업데이트 상품 이름2");

        UpdateDisplayProductWithProductsRequestDto requestDto = generateUpdateDisplayProductRequestDto(
                List.of(updateProductRequestDto1, updateProductRequestDto2));
        requestDto.setId(PRE_INSERTED_DISPLAY_PRODUCT_ID);
        requestDto.setName("업데이트 전시 상품 이름");

        //when
        productService.updateDisplayProductByIdWithProducts(requestDto);

        //then
        DisplayProductResponseDto response = productService.getDisplayProductById(
                PRE_INSERTED_DISPLAY_PRODUCT_ID);
        List<ProductResponseDto> products = response.getProducts();
        products.forEach(product -> {
            assertThat(product.getName()).isNotEqualTo(PRODUCT_NAME);
        });

        assertThat(response.getName()).isEqualTo("업데이트 전시 상품 이름");
    }

    @DisplayName("전시용 상품 수정 시 새로운 관련 상품이 추가될 수 있다.")
    @Test
    void updateDisplayProductByIdWithProducts_insert_new_product() {
        //given
        UpdateProductRequestDto updateProductRequestDto1 = generateUpdateProductRequestDto();
        updateProductRequestDto1.setId(PRE_INSERTED_PRODUCT_ID_1);

        UpdateProductRequestDto updateProductRequestDto2 = generateUpdateProductRequestDto();
        updateProductRequestDto1.setId(PRE_INSERTED_PRODUCT_ID_2);

        UpdateProductRequestDto updateProductRequestDtoNew = generateUpdateProductRequestDto();
        updateProductRequestDtoNew.setId(null);
        updateProductRequestDtoNew.setName("새로 추가된 상품");

        UpdateDisplayProductWithProductsRequestDto requestDto = generateUpdateDisplayProductRequestDto(
                List.of(updateProductRequestDto1, updateProductRequestDto2,
                        updateProductRequestDtoNew));

        //when
        productService.updateDisplayProductByIdWithProducts(requestDto);

        //then
        DisplayProductResponseDto displayProductResponseDto = productService.getDisplayProductById(
                PRE_INSERTED_DISPLAY_PRODUCT_ID);
        List<ProductResponseDto> productResponseDtos = displayProductResponseDto.getProducts();

        assertThat(productResponseDtos).hasSize(3);
    }

    @DisplayName("수정하고자 하는 전시용 상품이 실제로 존재해야 한다.")
    @Test
    void updateDisplayProductByIdWithProducts_no_product_exception() {
        //given
        UpdateProductRequestDto updateProductRequestDto1 = generateUpdateProductRequestDto();
        updateProductRequestDto1.setId(PRE_INSERTED_PRODUCT_ID_1);

        UpdateProductRequestDto updateProductRequestDto2 = generateUpdateProductRequestDto();
        updateProductRequestDto1.setId(PRE_INSERTED_PRODUCT_ID_2);

        UpdateDisplayProductWithProductsRequestDto displayProductRequestDto = generateUpdateDisplayProductRequestDto(
                List.of(updateProductRequestDto1, updateProductRequestDto2));

        long notExistId = 100000L;
        displayProductRequestDto.setId(notExistId);

        //then
        assertThatThrownBy(
                () -> productService.updateDisplayProductByIdWithProducts(displayProductRequestDto))
                .isInstanceOf(NoSuchProductException.class);
    }

    @DisplayName("관련된 실제 상품이 모두 삭제되면 전시용 상품도 삭제된다.")
    @Test
    void updateDisplayProductByIdWithProducts_delete_displayProduct_when_all_products_deleted() {
        //when
        UpdateProductRequestDto productRequestDto1 = generateUpdateProductRequestDto();
        productRequestDto1.setId(PRE_INSERTED_PRODUCT_ID_1);
        productRequestDto1.setDeleted(true);

        UpdateProductRequestDto productRequestDto2 = generateUpdateProductRequestDto();
        productRequestDto2.setId(PRE_INSERTED_PRODUCT_ID_2);
        productRequestDto2.setDeleted(true);

        UpdateDisplayProductWithProductsRequestDto displayProductRequestDto = generateUpdateDisplayProductRequestDto(
                List.of(productRequestDto1, productRequestDto2));

        productService.updateDisplayProductByIdWithProducts(displayProductRequestDto);

        //then
        assertThatThrownBy(
                () -> productService.getDisplayProductById(PRE_INSERTED_DISPLAY_PRODUCT_ID))
                .isInstanceOf(NoSuchProductException.class);
    }

    @DisplayName("정상적으로 전시용 상품을 삭제할 수 있다.")
    @Test
    void deleteDisplayProductById() {
        //when
        productService.deleteDisplayProductById(PRE_INSERTED_DISPLAY_PRODUCT_ID);

        //then
        assertThatThrownBy(
                () -> productService.getDisplayProductById(PRE_INSERTED_DISPLAY_PRODUCT_ID))
                .isInstanceOf(NoSuchProductException.class);
    }

    @DisplayName("존재하지 않는 전시용 상품을 삭제할 수는 없다.")
    @Test
    void deleteDisplayProductById_cant_delete_not_exist_display_product() {
        //when
        Long notExistId = 100000L;

        //then
        assertThatThrownBy(
                () -> productService.deleteDisplayProductById(notExistId))
                .isInstanceOf(NoSuchProductException.class);
    }
}