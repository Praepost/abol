package com.abol.abol.app.exception.dto;

import com.abol.abol.app.exception.CommonErrorCodes;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ErrorResponseBuilder {
    public Builder builder(){
        return new Builder();
    }

    @Data
    @Accessors(chain = true)
    public class Builder {
        private static final String ERROR_DESCRIPTION_HIDDEN = "Description was hidden. Contact support.";

        private String errorCode;
        private String description;
        private String message;

        private Builder() {
        }

        public ErrorResponse build() {
            return new ErrorResponse(buildErrorCode(), buildDescription(), buildMessage());
        }

        private String buildErrorCode() {
            if (!StringUtils.isEmpty(errorCode)) {
                return errorCode;
            } else {
                return CommonErrorCodes.GENERAL;
            }
        }

        private String buildDescription() {
            return description;
        }

        private String buildMessage() {
            if (!StringUtils.isEmpty(message)) {
                return message;
            }
            return "some error occurred";
        }
    }
}