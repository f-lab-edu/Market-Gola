package com.flab.marketgola.order.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.order.constant.TestOrderFactory;
import com.flab.marketgola.order.constant.TestOrderProductFactory;
import com.flab.marketgola.order.dto.request.CreateOrderRequestDto;
import com.flab.marketgola.order.service.OrderService;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.LoginUser;
import java.util.ArrayList;
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
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        setUpMockUserLogin();
    }


    @DisplayName("정상적인 주문 생성 요청을 처리할 수 있다.")
    @Test
    void createOrder() throws Exception {
        //given
        when(orderService.createOrder(anyLong(), any())).thenReturn(1L);

        CreateOrderRequestDto requestDto = TestOrderFactory.generalCreateRequest().build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(OrderController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("주문 요청시 주문 상품이 최소 1개 이상 존재해야 한다.")
    @Test
    void createOrder_need_at_least_one_product() throws Exception {
        //given
        CreateOrderRequestDto requestDto = TestOrderFactory.generalCreateRequest()
                .products(new ArrayList<>())
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(OrderController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("주문 요청시 배송자, 배송자 번호, 배송 주소는 필수 입력 사항이다.")
    @ParameterizedTest
    @MethodSource("invalidCreateOrderValues")
    void createOrder_need_delivery_info(String receiverName, String receiverPhone,
            String receiverAddress) throws Exception {

        //given
        CreateOrderRequestDto requestDto = TestOrderFactory.generalCreateRequest()
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .receiverAddress(receiverAddress)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(OrderController.BASE_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Arguments> invalidCreateOrderValues() {
        return Stream.of(
                Arguments.of("", "010-1234-5678", "서울시 강남구"),
                Arguments.of("홍길동", null, "서울시 강남구"),
                Arguments.of("홍길동", "010-1234-5678", " ")
        );
    }

    @DisplayName("주문 수량은 최소 1개 이상이어야 한다.")
    @Test
    void createOrder_at_least_1_count_for_order_product() throws Exception {
        //given
        CreateOrderRequestDto requestDto = TestOrderFactory.generalCreateRequest()
                .products(List.of(TestOrderProductFactory.generalCreateRequest().count(0).build()))
                .build();

        //when
        String content = objectMapper.writeValueAsString(requestDto);

        //then
        mockMvc.perform(post(OrderController.BASE_PATH)
                        .session(session)
                        .content(content)
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