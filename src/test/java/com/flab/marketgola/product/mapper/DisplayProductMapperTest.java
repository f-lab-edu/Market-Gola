package com.flab.marketgola.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.product.constant.DisplayProductCreator;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestDisplayProductListFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.domain.SortType;
import com.flab.marketgola.product.dto.request.GetDisplayProductsCondition;
import com.flab.marketgola.product.mapper.dto.DisplayProductListDto;
import com.flab.marketgola.product.service.ProductService;
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

    @DisplayName("카테고리별로 전시용 상품 리스트를 조회할 수 있다.")
    @Test
    void findByCategoryId() {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(
                new ProductService(displayProductRepository, productRepository));
        creator.name("아이템1").categoryID(1).create();
        creator.name("아이템2").categoryID(2).create();

        GetDisplayProductsCondition condition = TestDisplayProductListFactory.generalCondition()
                .build();
        //when
        DisplayProductListDto displayProductListDto = displayProductRepository.findByCategoryId(
                1, condition).get();

        //then
        List<DisplayProduct> displayProducts = displayProductListDto.getDisplayProducts();
        assertThat(displayProducts).hasSize(1);
        assertThat(displayProducts.get(0).getName()).isEqualTo("아이템1");
    }

    @DisplayName("카테고리별로 전시용 상품 리스트를 조회시 그 카테고리의 총 상품 갯수를 알 수 있다.")
    @Test
    void findByCategoryId_can_know_total_product_count() {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(
                new ProductService(displayProductRepository, productRepository));

        int productCnt = 10;
        int categoryId = 1;
        for (int i = 0; i < productCnt; i++) {
            creator.name("아이템1").categoryID(categoryId).create();
            creator.name("아이템2").categoryID(2).create();
        }

        GetDisplayProductsCondition condition = TestDisplayProductListFactory.generalCondition()
                .build();

        //when
        DisplayProductListDto displayProductListDto = displayProductRepository.findByCategoryId(
                categoryId, condition).get();

        //then
        assertThat(displayProductListDto.getTotal()).isEqualTo(productCnt);
    }

    @DisplayName("카테고리 별로 상품 리스트 조회시 가격순으로 정렬할 수 있다.")
    @ParameterizedTest
    @CsvSource({"PRICE_ASC,가장 싼,가장 비싼", "PRICE_DESC,가장 비싼,가장 싼"})
    void findByCategoryId_can_sort_by_price(SortType sortType, String expectedFirst,
            String expectedLast) {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(
                new ProductService(displayProductRepository, productRepository));
        creator.name("가장 싼").categoryID(1).price(1000).create();
        creator.name("가장 비싼").categoryID(1).price(3000).create();
        creator.name("가격 중간 ").categoryID(1).price(2000).create();

        GetDisplayProductsCondition condition = TestDisplayProductListFactory.generalCondition()
                .sortType(sortType)
                .build();

        //when
        DisplayProductListDto displayProductListDto = displayProductRepository.findByCategoryId(
                1, condition).get();

        //then
        List<DisplayProduct> displayProducts = displayProductListDto.getDisplayProducts();
        assertThat(displayProducts.get(0).getName()).isEqualTo(expectedFirst);
        assertThat(displayProducts.get(displayProducts.size() - 1).getName()).isEqualTo(
                expectedLast);
    }

    @DisplayName("카테고리 별로 상품 리스트 조회시 신상품 순으로 정렬할 수 있다.")
    @Test
    void findByCategoryId_can_sort_by_created_time() throws InterruptedException {
        //given
        DisplayProductCreator creator = new DisplayProductCreator(
                new ProductService(displayProductRepository, productRepository));

        creator.name("가장 오래된").categoryID(1).create();
        Thread.sleep(1000);
        creator.name("가장 최신").categoryID(1).create();

        GetDisplayProductsCondition condition = TestDisplayProductListFactory.generalCondition()
                .sortType(SortType.DATE_DESC)
                .build();

        //when
        DisplayProductListDto displayProductListDto = displayProductRepository.findByCategoryId(
                1, condition).get();

        //then
        List<DisplayProduct> displayProducts = displayProductListDto.getDisplayProducts();
        assertThat(displayProducts.get(0).getName()).isEqualTo("가장 최신");
        assertThat(displayProducts.get(displayProducts.size() - 1).getName()).isEqualTo("가장 오래된");
    }
}
