package com.abol.abol.app.sevices;


import com.abol.abol.app.contollers.FileStorageProperties;
import com.abol.abol.app.contollers.dto.FileMessage;
import com.abol.abol.app.exception.FileStorageException;
import com.abol.abol.app.exception.MyFileNotFoundException;
import com.abol.abol.app.models.CustomFiles;
import com.abol.abol.app.models.Person;
import com.abol.abol.app.repositories.FilesRepository;
import com.abol.abol.app.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FilesRepository filesRepository;
    private final JWTUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, FilesRepository filesRepository, JWTUtil jwtUtil, RestTemplate restTemplate) {
        this.filesRepository = filesRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            if(!fileName.contains(".jpg") && (!fileName.contains(".png"))){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Person person = (Person)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String token = jwtUtil.generateAccessToken(person);

            // if the authentication is successful
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", token);
            // Use Token to get Response
            HttpEntity<FileMessage> requestEntity = new HttpEntity<>(new FileMessage(person.getEmail(), Math.toIntExact(file.getSize())), headers);

            restTemplate.exchange("http://localhost:8080/mail/file", HttpMethod.GET, requestEntity, String.class);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public List<String> downloadFromAuthor(String author,Integer page, Integer size, String type) {
        Pageable pageable =  null;

        switch (type) {
            case  ("data"):
                pageable = PageRequest.of(page, size, Sort.by("data"));
                break;
            case ("id"):
                pageable = PageRequest.of(page, size, Sort.by("id"));
                break;
            case ("size"):
                pageable = PageRequest.of(page, size, Sort.by("size"));
                break;
        }

        Optional<List<CustomFiles>> files = filesRepository.findByAuthor(author, pageable);

        if(files.isEmpty()){
            throw new MyFileNotFoundException("File not found ");
        }
        List<CustomFiles> file = files.get();

        return file.stream().map(v->v.getFileDownloadUri()).collect(Collectors.toList());
    }
}