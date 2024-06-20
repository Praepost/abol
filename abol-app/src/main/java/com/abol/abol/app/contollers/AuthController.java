package com.abol.abol.app.contollers;

import com.abol.abol.app.contollers.dto.LoginDto;
import com.abol.abol.app.contollers.dto.PersonDto;
import com.abol.abol.app.contollers.dto.RefreshTokenDto;
import com.abol.abol.app.exception.FingerprintNotFoundException;
import com.abol.abol.app.exception.UserAlreadyExistAuthenticationException;
import com.abol.abol.app.exception.UserNotFoundException;
import com.abol.abol.app.security.dto.AccessTransfer;
import com.abol.abol.app.security.dto.AuthTransfer;
import com.abol.abol.app.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements IAuthController{
    @Lazy
    private final PersonService service;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody PersonDto personDto) throws UserAlreadyExistAuthenticationException {
        AuthTransfer authTransfer = service.register(personDto);

        return ResponseEntity.ok(authTransfer);
    }

    @Override
    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody LoginDto personDto) throws UserAlreadyExistAuthenticationException, UserNotFoundException {
        AuthTransfer authTransfer = service.authenticate(personDto);

        return ResponseEntity.ok(authTransfer);
    }

    @Override
    @PostMapping("/aces")
    public ResponseEntity createAcesToken(@RequestBody RefreshTokenDto personDto) throws UserAlreadyExistAuthenticationException, UserNotFoundException, FingerprintNotFoundException {
        AccessTransfer accessTransfer = service.createAccessToken(personDto.getRefreshToken());

        return ResponseEntity.ok(accessTransfer);
    }
}
