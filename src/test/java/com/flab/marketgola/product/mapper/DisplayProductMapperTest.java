package com.flab.marketgola.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
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
@SpringBootTest(classes = TestRedisConfiguration.class)
class DisplayProductMapperTest {

    @Autowired
    DisplayProductMapper displayProductRepository;

    @Autowired
    ProductMapper productRepository;

    @DisplayName("정상적으로 전시용 상품을 추가할 수 있다")
    @Test
    void insert() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();

        //when
        displayProductRepository.insert(displayProduct);

        Product product = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .build();

        productRepository.insert(product);

        //then
        assertThat(displayProductRepository.findById(displayProduct.getId())).isNotEmpty();
    }

    @DisplayName("전시용 상품을 찾으면 관련된 실제 삭제되지 않은 상품이 모두 포함된다.")
    @Test
    void findById() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();

        displayProductRepository.insert(displayProduct);

        Product product1 = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .build();

        Product product2 = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .build();

        Product productDeleted = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .isDeleted(true)
                .build();

        productRepository.insert(product1);
        productRepository.insert(product2);
        productRepository.insert(productDeleted);

        //when
        DisplayProduct findDisplayProduct = displayProductRepository.findById(
                displayProduct.getId()).get();

        //then
        assertThat(findDisplayProduct.getProducts()).hasSize(2);
    }

    @DisplayName("정상적으로 전시용 상품을 업데이트 할 수 있다.")
    @Test
    void update() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();
        displayProductRepository.insert(displayProduct);

        Product product = TestProductFactory.generalProduct().displayProduct(displayProduct)
                .build();
        productRepository.insert(product);

        //when
        String updateName = "업데이트";
        String updateMainImageName = "업데이트 이미지 이름";
        DisplayProduct updateDisplayProduct = DisplayProduct.builder()
                .id(displayProduct.getId())
                .name(updateName)
                .mainImageName(updateMainImageName)
                .build();

        displayProductRepository.update(updateDisplayProduct);

        //then
        DisplayProduct findDisplayProduct = displayProductRepository.findById(
                displayProduct.getId()).get();
        assertThat(findDisplayProduct.getName()).isEqualTo(updateName);
        assertThat(findDisplayProduct.getMainImageName()).isEqualTo(updateMainImageName);
    }
}
