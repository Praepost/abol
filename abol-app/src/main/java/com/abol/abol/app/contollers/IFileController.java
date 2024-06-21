package com.abol.abol.app.contollers;

import com.abol.abol.app.contollers.dto.UploadFileResponse;
import com.abol.abol.app.exception.dto.MessagesResponse;
import com.abol.abol.app.exception.dto.SuccessfulResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IFileController {

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при загрузки файла "),
    })
    UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file);

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при загрузки файла "),
    })
    List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files);

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при загрузки файлов "),

    })
    ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request);

    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при закачки файлов "),

    })
    ResponseEntity downloadFromModerator(@RequestParam("author") String author,
                                                @RequestParam ("page") Integer page,
                                                @RequestParam("size") Integer size,
                                                @RequestParam("type") String type);
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400, response = MessagesResponse.class, message =
                    "Ошибка при закачки файлов "),

    })
    @GetMapping("/download/FromAuthor")
    ResponseEntity downloadFromAuthor(@RequestParam ("page") Integer page,
                                      @RequestParam("size") Integer size,
                                      @RequestParam("type") String type);

}
