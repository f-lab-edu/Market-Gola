package com.flab.marketgola.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.product.constant.DisplayProductCreator;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestDisplayProductListFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.dto.request.GetDisplayProductsCondition;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.dto.response.ProductResponseDto;
import com.flab.marketgola.product.exception.NoSuchCategoryException;
import com.flab.marketgola.product.exception.NoSuchProductException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .product(TestProductFactory.generalCreateRequest().build())
                .build();

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
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .product(TestProductFactory.generalCreateRequest().build())
                .build();

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
        int notExistCategoryId = 100;
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .product(TestProductFactory.generalCreateRequest().build())
                .productCategoryId(notExistCategoryId)
                .build();

        //then
        assertThatThrownBy(() -> productService.createDisplayProductWithProducts(requestDto))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("정상적으로 전시용 상품을 찾을 수 있다.")
    @Test
    void getDisplayProductById() {
        //given
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .product(TestProductFactory.generalCreateRequest().build())
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                requestDto);

        //when
        DisplayProductResponseDto responseDto = productService.getDisplayProductById(
                createResponseDto.getId());

        //then
        assertThat(responseDto.getId()).isNotNull();
        assertThat(responseDto.getName()).isNotNull();
        assertThat(responseDto.getProducts()).isNotEmpty();
    }

    @DisplayName("전시용 상품의 가격은 관련된 상품 가격 중 최소값이다.")
    @Test
    void getDisplayProductById_price() {
        //given
        CreateProductRequestDto cheapProduct = TestProductFactory.generalCreateRequest()
                .price(1000)
                .build();

        CreateProductRequestDto expensiveProduct = TestProductFactory.generalCreateRequest()
                .price(2000)
                .build();

        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .products(List.of(cheapProduct, expensiveProduct))
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                requestDto);

        //when
        DisplayProductResponseDto responseDto = productService.getDisplayProductById(
                createResponseDto.getId());

        //then
        assertThat(responseDto.getPrice()).isEqualTo(1000);
    }

    @DisplayName("전시용 상품과 관련된 실제 상품은 함께 수정 된다.")
    @Test
    void updateDisplayProductByIdWithProducts() {
        //given
        CreateProductRequestDto createProductRequest = TestProductFactory.generalCreateRequest()
                .build();

        CreateDisplayProductRequestDto createDisplayProductRequest = TestDisplayProductFactory.generalCreateRequest()
                .product(createProductRequest)
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                createDisplayProductRequest);

        Long storedProductId = createResponseDto.getProducts().get(0).getId();
        UpdateProductRequestDto updateProductRequest = TestProductFactory.generalUpdateRequest()
                .id(storedProductId)
                .name("업데이트 상품 이름")
                .build();

        UpdateDisplayProductWithProductsRequestDto requestDto = TestDisplayProductFactory.generalUpdateRequest()
                .id(createResponseDto.getId())
                .name("업데이트 전시용 상품 이름")
                .product(updateProductRequest)
                .build();

        //when
        DisplayProductResponseDto updateResponse = productService.updateDisplayProductByIdWithProducts(
                requestDto);

        //then
        assertThat(updateResponse.getProducts().get(0).getName()).isEqualTo("업데이트 상품 이름");
        assertThat(updateResponse.getName()).isEqualTo("업데이트 전시용 상품 이름");
    }

    @DisplayName("전시용 상품 수정 시 새로운 관련 상품이 추가될 수 있다.")
    @Test
    void updateDisplayProductByIdWithProducts_insert_new_product() {
        //given
        CreateProductRequestDto createProductRequest = TestProductFactory.generalCreateRequest()
                .build();

        CreateDisplayProductRequestDto createDisplayProductRequest = TestDisplayProductFactory.generalCreateRequest()
                .product(createProductRequest)
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                createDisplayProductRequest);

        Long storedProductId = createResponseDto.getProducts().get(0).getId();
        UpdateProductRequestDto updateProduct = TestProductFactory.generalUpdateRequest()
                .id(storedProductId)
                .build();

        UpdateProductRequestDto updateProductNew = TestProductFactory.generalUpdateRequest()
                .id(null)
                .build();

        UpdateDisplayProductWithProductsRequestDto requestDto = TestDisplayProductFactory.generalUpdateRequest()
                .products(List.of(updateProduct, updateProductNew))
                .build();

        //when
        productService.updateDisplayProductByIdWithProducts(requestDto);

        //then
        DisplayProductResponseDto displayProductResponseDto = productService.getDisplayProductById(
                createResponseDto.getId());
        List<ProductResponseDto> productResponseDtos = displayProductResponseDto.getProducts();

        assertThat(productResponseDtos).hasSize(2);
    }

    @DisplayName("수정하고자 하는 전시용 상품이 실제로 존재해야 한다.")
    @Test
    void updateDisplayProductByIdWithProducts_no_product_exception() {
        //given
        CreateProductRequestDto createProductRequest = TestProductFactory.generalCreateRequest()
                .build();

        CreateDisplayProductRequestDto createDisplayProductRequest = TestDisplayProductFactory.generalCreateRequest()
                .product(createProductRequest)
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                createDisplayProductRequest);

        Long storedProductId = createResponseDto.getProducts().get(0).getId();
        UpdateProductRequestDto updateProduct = TestProductFactory.generalUpdateRequest()
                .id(storedProductId)
                .build();

        long notExistId = 100000L;
        UpdateDisplayProductWithProductsRequestDto reuqestDto = TestDisplayProductFactory.generalUpdateRequest()
                .id(notExistId)
                .product(updateProduct)
                .build();

        //then
        assertThatThrownBy(
                () -> productService.updateDisplayProductByIdWithProducts(reuqestDto))
                .isInstanceOf(NoSuchProductException.class);
    }

    @DisplayName("관련된 실제 상품이 모두 삭제되면 전시용 상품도 삭제된다.")
    @Test
    void updateDisplayProductByIdWithProducts_delete_displayProduct_when_all_products_deleted() {
        //given
        CreateProductRequestDto createProductRequest = TestProductFactory.generalCreateRequest()
                .build();

        CreateDisplayProductRequestDto createDisplayProductRequest = TestDisplayProductFactory.generalCreateRequest()
                .product(createProductRequest)
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                createDisplayProductRequest);

        Long storedProductId = createResponseDto.getProducts().get(0).getId();
        UpdateProductRequestDto updateProduct = TestProductFactory.generalUpdateRequest()
                .id(storedProductId)
                .isDeleted(true)
                .build();

        UpdateDisplayProductWithProductsRequestDto requestDto = TestDisplayProductFactory.generalUpdateRequest()
                .product(updateProduct)
                .build();

        //when
        productService.updateDisplayProductByIdWithProducts(requestDto);

        //then
        assertThatThrownBy(
                () -> productService.getDisplayProductById(createResponseDto.getId()))
                .isInstanceOf(NoSuchProductException.class);
    }

    @DisplayName("정상적으로 전시용 상품을 삭제할 수 있다.")
    @Test
    void deleteDisplayProductById() {
        //given
        CreateProductRequestDto createProductRequest = TestProductFactory.generalCreateRequest()
                .build();

        CreateDisplayProductRequestDto createDisplayProductRequest = TestDisplayProductFactory.generalCreateRequest()
                .product(createProductRequest)
                .build();

        DisplayProductResponseDto createResponseDto = productService.createDisplayProductWithProducts(
                createDisplayProductRequest);

        //when
        productService.deleteDisplayProductById(createResponseDto.getId());

        //then
        assertThatThrownBy(
                () -> productService.getDisplayProductById(createResponseDto.getId()))
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

    @DisplayName("카테고리로 상품 리스트 조회시 카테고리 내의 총 페이지 수를 알 수 있다.")
    @ParameterizedTest
    @CsvSource({"31,4", "30,3", "0,0"})
    void getDisplayProductListByCategoryId_return_totalPages(int totalCount,
            int expectedTotalPages) {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(productService);

        int categoryId = 1;
        for (int i = 0; i < totalCount; i++) {
            creator.categoryID(categoryId).create();
        }

        GetDisplayProductsCondition condition = TestDisplayProductListFactory.generalCondition()
                .perPage(10)
                .build();

        //when
        DisplayProductListResponseDto responseDto = productService.getDisplayProductListByCategory(
                categoryId, condition);

        //then
        assertThat(responseDto.getMeta().getTotalPages()).isEqualTo(expectedTotalPages);
    }

    @DisplayName("존재하지 않는 카테고리에 속한 상품 리스트 조회는 빈 목록을 리턴한다.")
    @Test
    void getDisplayProductListByCategoryId_category_should_exist() {
        //given
        int notExistCategoryId = 10000;

        GetDisplayProductsCondition condition = TestDisplayProductListFactory.generalCondition()
                .build();
        //when
        DisplayProductListResponseDto responseDto = productService.getDisplayProductListByCategory(
                notExistCategoryId, condition);

        //then
        assertThat(responseDto.getData().size()).isZero();
    }
}