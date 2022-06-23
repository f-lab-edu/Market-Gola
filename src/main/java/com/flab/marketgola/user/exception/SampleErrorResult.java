package com.flab.marketgola.user.exception;

import lombok.Data;

@Data
public class SampleErrorResult {

    private final String timestamp;
    private final int status;
    private final String detail;
    private final String path;
}
