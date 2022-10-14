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
public class DisplayProductMapperTest {

    @Autowired
    DisplayProductMapper displayProductRepository;

    @Autowired
    ProductMapper productMapper;

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
}
