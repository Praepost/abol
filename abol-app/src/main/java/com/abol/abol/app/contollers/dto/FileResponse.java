package com.abol.abol.app.contollers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {
    private String fileDownloadUri;
    private long size;
}
