package com.flab.marketgola.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.product.constant.DisplayProductCreator;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("unit")
@SpringBootTest(classes = TestRedisConfiguration.class)
class DisplayProductRepositoryTest {

    @Autowired
    DisplayProductRepository displayProductRepository;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager em;

    @DisplayName("정상적으로 전시용 상품을 추가할 수 있다")
    @Test
    void save() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();

        //when
        displayProductRepository.save(displayProduct);

        Product product = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .build();

        productRepository.save(product);

        //then
        assertThat(displayProductRepository.findById(displayProduct.getId())).isNotEmpty();
    }

    @DisplayName("전시용 상품을 찾으면 관련된 실제 삭제되지 않은 상품이 모두 포함된다.")
    @Test
    void findByIdWithAll() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();

        displayProductRepository.save(displayProduct);

        Product product1 = TestProductFactory.generalProduct().displayProduct(displayProduct)
                .build();

        Product product2 = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .build();

        Product productDeleted = TestProductFactory.generalProduct()
                .displayProduct(displayProduct)
                .isDeleted(true)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(productDeleted);

        em.flush();
        em.clear();

        //when
        DisplayProduct findDisplayProduct = displayProductRepository.findByIdWithAll(
                displayProduct.getId()).get();

        //then
        assertThat(findDisplayProduct.getProducts()).hasSize(2);
    }

    @DisplayName("정상적으로 전시용 상품을 업데이트 할 수 있다.")
    @Test
    void update() {
        //given
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();
        displayProductRepository.save(displayProduct);

        Product product = TestProductFactory.generalProduct().displayProduct(displayProduct)
                .build();
        productRepository.save(product);

        //when
        displayProduct.changeName("업데이트");

        em.flush();
        em.clear();

        //then
        DisplayProduct findDisplayProduct = displayProductRepository
                .findById(displayProduct.getId()).get();

        assertThat(findDisplayProduct.getName()).isEqualTo("업데이트");
    }

    @DisplayName("카테고리별로 전시용 상품 리스트를 조회할 수 있다.")
    @Test
    void findByCategoryId() {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(displayProductRepository,
                productRepository);
        creator.name("아이템1").categoryID(1).create();
        creator.name("아이템2").categoryID(2).create();

        PageRequest pageRequest = PageRequest.of(0, 50); //첫번째 페이지를 가져오고, 사이즈는 10개씩

        //when
        Page<DisplayProduct> displayProductPage = displayProductRepository.findByCategory(
                1, pageRequest);

        List<DisplayProduct> displayProducts = displayProductPage.getContent();

        //then
        assertThat(displayProducts).hasSize(1);
        assertThat(displayProducts.get(0).getName()).isEqualTo("아이템1");
    }

    @DisplayName("카테고리별로 전시용 상품 리스트를 조회시 그 카테고리의 총 상품 갯수를 알 수 있다.")
    @Test
    void findByCategoryId_can_know_total_product_count() {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(displayProductRepository,
                productRepository);

        int productCnt = 10;
        for (int i = 0; i < productCnt; i++) {
            creator.name("아이템1").categoryID(1).create();
            creator.name("아이템2").categoryID(2).create();
        }

        PageRequest pageRequest = PageRequest.of(0, 50);

        //when
        Page<DisplayProduct> displayProductPage = displayProductRepository.findByCategory(
                1, pageRequest);

        //then
        assertThat(displayProductPage.getTotalElements()).isEqualTo(productCnt);
    }

    @DisplayName("카테고리 별로 상품 리스트 조회시 가격순으로 정렬할 수 있다.")
    @ParameterizedTest
    @MethodSource("sortByPriceASCAndDESC")
    void findByCategoryId_can_sort_by_price(Sort sortType, String expectedFirst,
            String expectedLast) {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(displayProductRepository,
                productRepository);
        creator.name("가장 싼").categoryID(1).price(1000).create();
        creator.name("가장 비싼").categoryID(1).price(3000).create();
        creator.name("가격 중간").categoryID(1).price(2000).create();

        PageRequest pageRequest = PageRequest.of(0, 50, sortType);

        //when
        Page<DisplayProduct> displayProductPage = displayProductRepository.findByCategory(1,
                pageRequest);

        //then
        List<DisplayProduct> displayProducts = displayProductPage.getContent();
        assertThat(displayProducts.get(0).getName()).isEqualTo(expectedFirst);
        assertThat(displayProducts.get(displayProducts.size() - 1).getName()).isEqualTo(
                expectedLast);
    }

    private static Stream<Arguments> sortByPriceASCAndDESC() {
        return Stream.of(
                Arguments.of(Sort.by("price").ascending(), "가장 싼", "가장 비싼"),
                Arguments.of(Sort.by("price").descending(), "가장 비싼", "가장 싼")
        );
    }

    @DisplayName("카테고리 별로 상품 리스트 조회시 신상품 순으로 정렬할 수 있다.")
    @Test
    void findByCategoryId_can_sort_by_created_time() throws InterruptedException {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(
                displayProductRepository, productRepository);

        creator.name("가장 오래된").categoryID(1).create();
        Thread.sleep(1000); //동일한 시간으로 생성되서 정확히 테스트가 안되므로 1초 기다리기
        creator.name("가장 최신").categoryID(1).create();

        PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Direction.DESC, "createdAt"));

        //when
        Page<DisplayProduct> displayProductPage = displayProductRepository.findByCategory(1,
                pageRequest);

        //then
        List<DisplayProduct> displayProducts = displayProductPage.getContent();
        assertThat(displayProducts.get(0).getName()).isEqualTo("가장 최신");
        assertThat(displayProducts.get(displayProducts.size() - 1).getName()).isEqualTo("가장 오래된");
    }
}