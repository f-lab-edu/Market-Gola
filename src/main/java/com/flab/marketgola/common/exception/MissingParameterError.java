package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class MissingParameterError implements ApiSubError {

    String missingParameter;
    String missingParameterType;

    public MissingParameterError(String missingParameter, String missingParameterType) {
        this.missingParameter = missingParameter;
        this.missingParameterType = missingParameterType;

    }
}
