package com.abol.abol.app.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthTransfer {
    private String jwtAccess;
    private String jwtRefresh;
}
