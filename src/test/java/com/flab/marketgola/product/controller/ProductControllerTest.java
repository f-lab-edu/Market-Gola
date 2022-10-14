package com.flab.marketgola.product.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static com.flab.marketgola.product.constant.TestDisplayProductFactory.CATEGORY_ID;
import static com.flab.marketgola.product.constant.TestDisplayProductFactory.DESCRIPTION_IMAGE_NAME;
import static com.flab.marketgola.product.constant.TestDisplayProductFactory.DISPLAY_PRODUCT_NAME;
import static com.flab.marketgola.product.constant.TestDisplayProductFactory.MAIN_IMAGE_NAME;
import static com.flab.marketgola.product.constant.TestDisplayProductFactory.PRE_INSERTED_DISPLAY_PRODUCT_ID;
import static com.flab.marketgola.product.constant.TestProductFactory.PRE_INSERTED_PRODUCT_ID_1;
import static com.flab.marketgola.product.constant.TestProductFactory.PRICE;
import static com.flab.marketgola.product.constant.TestProductFactory.PRODUCT_NAME;
import static com.flab.marketgola.product.constant.TestProductFactory.STOCK;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.service.ProductService;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.LoginUser;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("unit")
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        setUpMockUserLogin();
    }

    @DisplayName("정상적인 상품 저장 요청을 처리할 수 있다.")
    @Test
    void createDisplayProduct() throws Exception {
        //given
        when(productService.createDisplayProductWithProducts(any()))
                .thenReturn(DisplayProductResponseDto.builder().id(1L).build());

        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .product(TestProductFactory.generalCreateRequest().build())
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("상품 저장시 관련된 실제 상품을 최소 1개 이상 존재해야 한다.")
    @Test
    void createDisplayProduct_need_at_least_one_product() throws Exception {
        //given
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("상품 저장시 관련된 전시용 상품의 값이 유효해야 한다.")
    @ParameterizedTest
    @MethodSource("invalidCreateDisplayProductValues")
    void createDisplayProduct_invalid_values_bad_request(
            String name, String mainImageName, String descriptionImageName,
            int productCategoryId)
            throws Exception {

        //given
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .name(name)
                .mainImageUrl(mainImageName)
                .descriptionImageUrl(descriptionImageName)
                .productCategoryId(productCategoryId)
                .product(TestProductFactory.generalCreateRequest().build())
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> invalidCreateDisplayProductValues() {
        return Stream.of(
                Arguments.of(null, MAIN_IMAGE_NAME, DESCRIPTION_IMAGE_NAME, CATEGORY_ID),

                Arguments.of(DISPLAY_PRODUCT_NAME, null, DESCRIPTION_IMAGE_NAME, CATEGORY_ID),

                Arguments.of(DISPLAY_PRODUCT_NAME, DISPLAY_PRODUCT_NAME, null, CATEGORY_ID),

                Arguments.of(DISPLAY_PRODUCT_NAME, MAIN_IMAGE_NAME, DESCRIPTION_IMAGE_NAME, 0)
        );
    }

    @DisplayName("상품 저장시 관련된 실제 상품의 값들이 유효해야 한다.")
    @ParameterizedTest
    @MethodSource("invalidCreateProductValues")
    void createDisplayProduct_invalid_values_bad_request(String name, int price, int stock)
            throws Exception {
        //given
        CreateDisplayProductRequestDto requestDto = TestDisplayProductFactory.generalCreateRequest()
                .product(new CreateProductRequestDto(name, price, stock))
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> invalidCreateProductValues() {
        return Stream.of(
                Arguments.of(null, PRICE, STOCK),
                Arguments.of(PRODUCT_NAME, -100, STOCK),
                Arguments.of(PRODUCT_NAME, 10, STOCK),
                Arguments.of(PRODUCT_NAME, PRICE, -100));
    }

    @DisplayName("정상적인 상품 수정 요청을 처리할 수 있다.")
    @Test
    void updateDisplayProduct() throws Exception {
        //given
        when(productService.updateDisplayProductByIdWithProducts(any()))
                .thenReturn(DisplayProductResponseDto.builder().id(1L).build());

        UpdateDisplayProductWithProductsRequestDto requestDto = TestDisplayProductFactory.generalUpdateRequest()
                .product(TestProductFactory.generalUpdateRequest().build())
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(put(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("상품 수정시 전시용 상품의 값이 유효 해야 한다.")
    @ParameterizedTest
    @MethodSource("invalidUpdateDisplayProductValues")
    void updateDisplayProduct_invalid_values_bad_request(
            Long id, String name, String mainImageName, String descriptionImageName,
            int productCategoryId)
            throws Exception {

        //given
        UpdateDisplayProductWithProductsRequestDto requestDto = UpdateDisplayProductWithProductsRequestDto.builder()
                .id(id)
                .name(name)
                .mainImageUrl(mainImageName)
                .descriptionImageUrl(descriptionImageName)
                .productCategoryId(productCategoryId)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(put(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> invalidUpdateDisplayProductValues() {
        return Stream.of(
                Arguments.of(null, DISPLAY_PRODUCT_NAME, MAIN_IMAGE_NAME, DESCRIPTION_IMAGE_NAME,
                        CATEGORY_ID),

                Arguments.of(PRE_INSERTED_DISPLAY_PRODUCT_ID, null, MAIN_IMAGE_NAME,
                        DESCRIPTION_IMAGE_NAME, CATEGORY_ID),

                Arguments.of(PRE_INSERTED_DISPLAY_PRODUCT_ID, DISPLAY_PRODUCT_NAME, null,
                        DESCRIPTION_IMAGE_NAME, CATEGORY_ID),

                Arguments.of(PRE_INSERTED_DISPLAY_PRODUCT_ID, DISPLAY_PRODUCT_NAME,
                        DISPLAY_PRODUCT_NAME, null, CATEGORY_ID),

                Arguments.of(PRE_INSERTED_DISPLAY_PRODUCT_ID, DISPLAY_PRODUCT_NAME, MAIN_IMAGE_NAME,
                        DESCRIPTION_IMAGE_NAME, 0)
        );
    }

    @DisplayName("상품 수정시 관련된 실제 상품의 값들이 유효해야 한다.")
    @ParameterizedTest
    @MethodSource("invalidUpdateProductValues")
    void updateDisplayProduct_invalid_values_bad_request(String name, int price, int stock)
            throws Exception {

        //given
        UpdateProductRequestDto productRequestDto = UpdateProductRequestDto.builder()
                .id(PRE_INSERTED_PRODUCT_ID_1)
                .name(name)
                .price(price)
                .stock(stock)
                .build();

        UpdateDisplayProductWithProductsRequestDto requestDto = TestDisplayProductFactory.generalUpdateRequest()
                .product(productRequestDto)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(put(ProductController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> invalidUpdateProductValues() {
        return Stream.of(
                Arguments.of(null, PRICE, STOCK),
                Arguments.of(PRODUCT_NAME, -100, STOCK),
                Arguments.of(PRODUCT_NAME, 10, STOCK),
                Arguments.of(PRODUCT_NAME, PRICE, -100),
                Arguments.of(PRODUCT_NAME, PRICE, -100));
    }

    private void setUpMockUserLogin() {
        session = new MockHttpSession();
        LoginUser loginUser = new LoginUser(1L, TestUserFactory.NAME);
        session.setAttribute(LOGIN_KEY, loginUser);
    }
}