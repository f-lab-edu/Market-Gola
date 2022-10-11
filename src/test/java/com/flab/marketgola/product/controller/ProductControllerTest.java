package com.flab.marketgola.product.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static com.flab.marketgola.product.constant.TestDisplayProductFactory.*;
import static com.flab.marketgola.product.constant.TestProductFactory.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;
import com.flab.marketgola.product.dto.response.DisplayProductResponseDto;
import com.flab.marketgola.product.service.ProductService;
import com.flab.marketgola.user.constant.ValidUser;
import com.flab.marketgola.user.domain.LoginUser;
import java.util.Arrays;
import java.util.List;
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

        CreateDisplayProductRequestDto requestDto = generateCreateDisplayProductRequestDto(
                List.of(generateCreateProductRequestDto()));

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

    @DisplayName("상품 저장시 관련된 전시용 상품의 값이 유효해야 한다.")
    @ParameterizedTest
    @MethodSource("invalidCreateDisplayProductValues")
    void createDisplayProduct_invalid_values_bad_request(
            String name, String mainImageName, String descriptionImageName,
            int productCategoryId, List<CreateProductRequestDto> products)
            throws Exception {

        //given
        CreateDisplayProductRequestDto requestDto = CreateDisplayProductRequestDto.builder()
                .name(name)
                .mainImageName(mainImageName)
                .descriptionImageName(descriptionImageName)
                .productCategoryId(productCategoryId)
                .products(products)
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
                Arguments.of(null, MAIN_IMAGE_NAME, DESCRIPTION_IMAGE_NAME, CATEGORY_ID,
                        Arrays.asList(generateCreateProductRequestDto())),

                Arguments.of(DISPLAY_PRODUCT_NAME, null, DESCRIPTION_IMAGE_NAME, CATEGORY_ID,
                        Arrays.asList(generateCreateProductRequestDto())),

                Arguments.of(DISPLAY_PRODUCT_NAME, DISPLAY_PRODUCT_NAME, null, CATEGORY_ID,
                        Arrays.asList(generateCreateProductRequestDto())),

                Arguments.of(DISPLAY_PRODUCT_NAME, MAIN_IMAGE_NAME, DESCRIPTION_IMAGE_NAME, 0,
                        Arrays.asList(generateCreateProductRequestDto())),

                Arguments.of(DISPLAY_PRODUCT_NAME, MAIN_IMAGE_NAME, DESCRIPTION_IMAGE_NAME,
                        CATEGORY_ID, null)
        );
    }

    @DisplayName("상품 저장시 관련된 실제 상품의 값들이 유효해야 한다.")
    @ParameterizedTest
    @MethodSource("invalidCreateProductValues")
    void createDisplayProduct_invalid_values_bad_request(String name, int price, int stock)
            throws Exception {
        //given
        CreateDisplayProductRequestDto requestDto = generateCreateDisplayProductRequestDto(
                Arrays.asList(new CreateProductRequestDto(name, price, stock)));

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

    private void setUpMockUserLogin() {
        session = new MockHttpSession();
        LoginUser loginUser = new LoginUser(1L, ValidUser.NAME);
        session.setAttribute(LOGIN_KEY, loginUser);
    }
}