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
class ProductMapperTest {

    @Autowired
    ProductMapper productRepository;

    @Autowired
    DisplayProductMapper displayProductRepository;

    @DisplayName("정상적으로 상품을 추가할 수 있다")
    @Test
    void insert() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();
        displayProductRepository.insert(displayProduct);

        Product product = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .build();

        //when
        productRepository.insert(product);

        //then
        assertThat(productRepository.findById(product.getId())).isNotEmpty();
    }
}