package com.flab.marketgola.product.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto;
import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto.DisplayProductListData;
import com.flab.marketgola.product.dto.response.DisplayProductListResponseDto.Pagination;
import com.flab.marketgola.product.service.ProductService;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.LoginUser;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("unit")
@WebMvcTest(ProductListController.class)
class ProductListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        setUpMockUserLogin();
    }


    @DisplayName("정상적인 상품 페이지 조회 요청을 처리할 수 잇다. ")
    @Test
    void getDisplayProductsByCategory() throws Exception {
        //given
        DisplayProductListData data = new DisplayProductListData(1, "이름", "url", 1000);
        Pagination meta = new Pagination(1, 1, 1);

        DisplayProductListResponseDto displayProductListDto = new DisplayProductListResponseDto(
                List.of(data), meta);

        Mockito.when(productService.getDisplayProductListByCategory(anyInt(), any()))
                .thenReturn(displayProductListDto);

        //then
        mockMvc.perform(get("/categories/1/products")
                        .session(session)
                        .param("sortType", "price_asc")
                        .param("page", "1")
                        .param("perPage", "50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].price").value(1000))
                .andExpect(jsonPath("$.meta.total").value(1))
                .andExpect(jsonPath("$.meta.totalPages").value(1))
                .andDo(print());
    }

    @DisplayName("상품 페이지 조회시 페이지와 페이지 당 상품 수는 1 이상 이어야 한다.")
    @ParameterizedTest
    @CsvSource({"-1,50", "1,-1"})
    void getDisplayProductsByCategory_page_and_perPage_at_least_1(String page, String perPage)
            throws Exception {
        mockMvc.perform(get("/categories/1/products")
                        .session(session)
                        .param("sortType", "price_asc")
                        .param("page", page)
                        .param("perPage", perPage)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private void setUpMockUserLogin() {
        session = new MockHttpSession();
        LoginUser loginUser = new LoginUser(1L, TestUserFactory.NAME);
        session.setAttribute(LOGIN_KEY, loginUser);
    }
}