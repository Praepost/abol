package com.abol.abol.app.contollers;

import com.abol.abol.app.contollers.dto.LoginDto;
import com.abol.abol.app.contollers.dto.PersonDto;
import com.abol.abol.app.contollers.dto.RefreshTokenDto;
import com.abol.abol.app.exception.FingerprintNotFoundException;
import com.abol.abol.app.exception.UserAlreadyExistAuthenticationException;
import com.abol.abol.app.exception.UserNotFoundException;
import com.abol.abol.app.exception.dto.MessagesResponse;
import com.abol.abol.app.exception.dto.SuccessfulResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface IAuthController {
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при регистрации "),
    })
    ResponseEntity register(PersonDto personDto) throws UserAlreadyExistAuthenticationException;

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при авторизации "),
    })
    ResponseEntity authenticate(LoginDto personDto) throws UserAlreadyExistAuthenticationException, UserNotFoundException;

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при создании ацесс токена "),
    })
    ResponseEntity createAcesToken(RefreshTokenDto personDto) throws UserAlreadyExistAuthenticationException, UserNotFoundException, FingerprintNotFoundException;
}
