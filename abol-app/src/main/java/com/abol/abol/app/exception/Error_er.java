package com.abol.abol.app.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Error_er {
    @ApiModelProperty(notes = "Error code you can use in your program to process the error somehow", required = true)
    private final String code;

    @ApiModelProperty(notes = "The most verbose description of the error we can get for you. This field is always in English. " +
            "You should not show it to the user", required = true)
    private final String description;

    @ApiModelProperty(notes = "This localized message is for the user", required = true)
    private final String message;
}