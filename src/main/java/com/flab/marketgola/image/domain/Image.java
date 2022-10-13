package com.flab.marketgola.image.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public abstract class Image {

    protected static final String IMAGE_DOMAIN = "https://www.image.gola.com";

    protected InputStream imageInputStream;

    protected String storedName;

    protected Image(InputStream imageInputStream, String originalFileName) {
        this.imageInputStream = imageInputStream;
        this.storedName = createStoredName(originalFileName);
    }

    public abstract String getStoreKey();

    private String createStoredName(String originalFileName) {
        return UUID.randomUUID() + "." + originalFileName.split("\\.")[1];
    }

    public void closeInputStream() {
        try {
            imageInputStream.close();
        } catch (IOException ex) {
            log.warn("파일 InputStream Close IOException", ex);
        }
    }
}

