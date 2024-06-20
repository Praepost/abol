package com.abol.abol.app.exception.dto;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public class MessagesResponse extends SuccessfulResponse {
    @NonNull
    private String message;
}
