package com.flab.marketgola.image.domain;

import com.flab.marketgola.product.controller.ProductController;
import java.io.InputStream;

public class DescriptionImage extends Image {

    private static final String STORE_PATH = ProductController.BASE_PATH;

    public static String generateUrl(String storedName) {
        return IMAGE_DOMAIN + STORE_PATH + "/" + storedName;
    }

    public String getStoreKey() {
        return STORE_PATH + "/" + storedName;
    }

    public DescriptionImage(InputStream imageInputStream, String originalFileName) {
        super(imageInputStream, originalFileName);
    }
}
