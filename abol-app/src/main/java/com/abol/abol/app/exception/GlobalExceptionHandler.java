package com.abol.abol.app.exception;

import com.abol.abol.app.exception.dto.ErrorResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private ErrorResponseBuilder errorResponseBuilder;

//    @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
//    public ResponseEntity<ErrorResponse> handleClientManipulationException(RuntimeException ex, WebRequest request) {
//        log.error("заглушка под стрим ерроры", ex);
//        ErrorResponse errorResponse = errorResponseBuilder.builder()
//                .setErrorCode(CommonErrorCodes.OPERATION_PERFORM_ERROR)
//                .setDescription("ошибка манипуляции данными: " + ex.getMessage())
//                .setMessage("операция прервана")
//                .build();
//        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
//    }
}