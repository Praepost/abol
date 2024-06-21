package com.abol.abol.app.contollers;

import com.abol.abol.app.contollers.dto.UploadFileResponse;
import com.abol.abol.app.models.CustomFiles;
import com.abol.abol.app.models.Person;
import com.abol.abol.app.repositories.FilesRepository;
import com.abol.abol.app.sevices.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FileController implements IFileController{

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileStorageService fileStorageService;
    private final FilesRepository filesRepository;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        CustomFiles customFiles = new CustomFiles();
        customFiles.setFileName(fileName);
        customFiles.setFileDownloadUri(fileDownloadUri);
        customFiles.setAuthor(username);
        customFiles.setTimeMills(System.currentTimeMillis());
        customFiles.setSize(Math.toIntExact(file.getSize()));

        filesRepository.save(customFiles);

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @GetMapping("/download/FromModerator")
    public ResponseEntity downloadFromModerator(@RequestParam("author") String author,
                                                       @RequestParam ("page") Integer page,
                                                       @RequestParam("size") Integer size,
                                                        @RequestParam("type") String type){
        List<String> files = fileStorageService.downloadFromAuthor(author,page,size,type);

        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/FromAuthor")
    public ResponseEntity downloadFromAuthor(@RequestParam ("page") Integer page,
                                             @RequestParam("size") Integer size,
                                             @RequestParam("type") String type){

        Person author = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> files = fileStorageService.downloadFromAuthor(author.getUsername(),page,size,type);

        return ResponseEntity.ok(files);
    }

}