package com.abol.abol.app.contollers;

import com.abol.abol.app.contollers.dto.FileMessage;
import com.abol.abol.app.contollers.dto.RegisterMessage;
import com.abol.abol.app.exception.dto.MessagesResponse;
import com.abol.abol.app.exception.dto.SuccessfulResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface IMessagePublisher {

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при публикации "),
    })
    ResponseEntity publishRegisterMessage(RegisterMessage message);
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при загрузки файла "),
    })
    ResponseEntity publishFileMessage(FileMessage message);
}