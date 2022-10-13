package com.flab.marketgola.image.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amazonaws.SdkClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.image.constant.TestImageFactory;
import com.flab.marketgola.image.service.ImageService;
import com.flab.marketgola.product.controller.ProductController;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.LoginUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("unit")
@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageService imageService;

    @Autowired
    ObjectMapper objectMapper;

    MockHttpSession session;

    @BeforeEach
    void init() {
        setUpMockUserLogin();
    }

    @DisplayName("정상적인 상품 이미지 저장 요청의 경우 이미지 저장에 성공한다.")
    @Test
    void uploadProductImages() throws Exception {
        //given
        when(imageService.upload(any()))
                .thenReturn(TestImageFactory.MAIN_IMAGE_STORED_NAME);

        MockMultipartFile mainImageFile = new MockMultipartFile("mainImage", "mainImage.png", "",
                TestImageFactory.getMainImageBytes());
        MockMultipartFile descriptionImageFile = new MockMultipartFile("descriptionImage",
                "descriptionImage.png", "",
                TestImageFactory.getDescriptionImageBytes());

        //then
        mockMvc.perform(multipart(ImageController.BASE_PATH + ProductController.BASE_PATH)
                        .file(mainImageFile)
                        .file(descriptionImageFile)
                        .session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("메인 이미지와 상세 설명 이미지 둘 중 하나라도 없는 경우 이미지 저장에 실패한다.")
    @Test
    void uploadProductImages_need_all_image() throws Exception {
        //given
        MockMultipartFile mainImageFile = new MockMultipartFile("mainImage",
                TestImageFactory.getMainImageBytes());

        //then
        mockMvc.perform(multipart(ImageController.BASE_PATH + ProductController.BASE_PATH)
                        .file(mainImageFile)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("두 이미지 중 하나라도 저장에 실패하면 요청은 실패한다.")
    @Test
    void uploadProductImages_need_all_image_upload_success() throws Exception {
        //given
        MockMultipartFile mainImageFile = new MockMultipartFile("mainImage",
                TestImageFactory.getMainImageBytes());
        MockMultipartFile descriptionImageFile = new MockMultipartFile("descriptionImage",
                TestImageFactory.getDescriptionImageBytes());

        when(imageService.upload(any()))
                .thenReturn(TestImageFactory.MAIN_IMAGE_STORED_NAME)
                .thenThrow(new SdkClientException(""));

        //then
        mockMvc.perform(multipart(ImageController.BASE_PATH + ProductController.BASE_PATH)
                        .file(mainImageFile)
                        .file(descriptionImageFile)
                        .session(session))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    private void setUpMockUserLogin() {
        session = new MockHttpSession();
        LoginUser loginUser = new LoginUser(1L, TestUserFactory.NAME);
        session.setAttribute(LOGIN_KEY, loginUser);
    }
}