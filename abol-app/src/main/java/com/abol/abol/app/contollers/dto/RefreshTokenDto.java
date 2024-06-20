package com.abol.abol.app.contollers.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class RefreshTokenDto {
    @NonNull
    private String refreshToken;
}
