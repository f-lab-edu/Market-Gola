package com.flab.marketgola.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("unit")
@SpringBootTest
public class DisplayProductMapperTest {

    @Autowired
    DisplayProductMapper displayProductRepository;

    @Autowired
    ProductCategoryMapper categoryMapper;

    @Autowired
    ProductMapper productMapper;

    @DisplayName("정상적으로 전시용 상품을 추가할 수 있다")
    @Test
    void insert() {
        //given
        DisplayProduct displayProduct = createDisplayProduct();

        //when
        displayProductRepository.insert(displayProduct);
        Product product = createRelatedProduct(displayProduct);
        productMapper.insert(product);

        //then
        assertThat(displayProductRepository.findById(displayProduct.getId())).isNotEmpty();
    }

    @DisplayName("전시용 상품을 찾으면 관련된 실제 삭제되지 않은 상품이 모두 포함된다.")
    @Test
    void findById() {
        //when
        DisplayProduct displayProduct = displayProductRepository.findById(1L).get();

        //then
        assertThat(displayProduct.getProducts()).hasSize(2);
    }

    @DisplayName("정상적으로 전시용 상품을 업데이트 할 수 있다.")
    @Test
    void update() {
        //when
        Long updateId = 1L;
        String updateName = "업데이트";
        String updateMainImageName = "업데이트 이미지 이름";
        DisplayProduct updateDisplayProduct = DisplayProduct.builder()
                .id(updateId)
                .name(updateName)
                .mainImageName(updateMainImageName)
                .build();

        displayProductRepository.update(updateDisplayProduct);

        //then
        DisplayProduct findDisplayProduct = displayProductRepository.findById(updateId).get();
        assertThat(findDisplayProduct.getName()).isEqualTo(updateName);
        assertThat(findDisplayProduct.getMainImageName()).isEqualTo(updateMainImageName);
    }

    private DisplayProduct createDisplayProduct() {
        return DisplayProduct.builder()
                .name(TestDisplayProductFactory.DISPLAY_PRODUCT_NAME)
                .descriptionImageName(TestDisplayProductFactory.DESCRIPTION_IMAGE_NAME)
                .category(TestDisplayProductFactory.CATEGORY)
                .mainImageName(TestDisplayProductFactory.MAIN_IMAGE_NAME)
                .build();
    }

    private Product createRelatedProduct(DisplayProduct displayProduct) {
        return Product.builder()
                .name(TestProductFactory.PRODUCT_NAME)
                .price(TestProductFactory.PRICE)
                .stock(TestProductFactory.STOCK)
                .isDeleted(TestProductFactory.IS_DELETED)
                .displayProduct(displayProduct)
                .build();
    }
}
