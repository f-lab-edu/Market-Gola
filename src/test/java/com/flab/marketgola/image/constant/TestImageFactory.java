package com.flab.marketgola.image.constant;

import com.flab.marketgola.image.domain.DescriptionImage;
import com.flab.marketgola.image.domain.MainImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestImageFactory {

    public static final String MAIN_IMAGE_NAME = "mainImage.png";
    public static final String DESCRIPTION_IMAGE_NAME = "descriptionImage.png";
    public static String MAIN_IMAGE_PATH = "src/test/resources/uploadImage.png";
    public static String DESCRIPTION_IMAGE_PATH = "src/test/resources/descriptionImage.png";
    public static final String MAIN_IMAGE_STORED_NAME = "26907afe-e095-49db-a2f7-45ae4f213456.png";
    public static final String DESCRIPTION_IMAGE_STORED_NAME = "94b2d3cf-a18d-48a0-98da-906a41ea02db.png";

    public static byte[] getMainImageBytes() throws IOException {
        return new FileInputStream(MAIN_IMAGE_PATH).readAllBytes();
    }

    public static byte[] getDescriptionImageBytes() throws IOException {
        return new FileInputStream(DESCRIPTION_IMAGE_PATH).readAllBytes();
    }

    public static MainImage getMainImage() throws FileNotFoundException {
        return new MainImage(new FileInputStream(MAIN_IMAGE_PATH), MAIN_IMAGE_NAME);
    }

    public static DescriptionImage getDescriptionImage() throws FileNotFoundException {
        return new DescriptionImage(new FileInputStream(DESCRIPTION_IMAGE_PATH),
                DESCRIPTION_IMAGE_NAME);
    }
}
