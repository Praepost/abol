package com.abol.abol.app.contollers.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class LoginDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
}

