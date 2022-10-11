package com.flab.marketgola.common.util;


import com.flab.marketgola.common.domain.ServiceType;

public class ImageUrlUtil {

    public static final String IMAGE_DOMAIN = "https://www.image.gola.com";
    public static final String PRODUCT_DIRECTORY = "products";
    public static final String SEPERATOR = "/";

    private ImageUrlUtil() {
    }

    public static String generateUrl(ServiceType type, String name) {
        StringBuilder url = new StringBuilder()
                .append(IMAGE_DOMAIN)
                .append(SEPERATOR);

        switch (type) {
            case PRODUCT:
                url.append(PRODUCT_DIRECTORY).append(SEPERATOR);
        }

        return url.append(name).toString();
    }

}
