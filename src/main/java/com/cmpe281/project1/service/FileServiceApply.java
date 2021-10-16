package com.cmpe281.project1.service;

import com.cmpe281.project1.repositories.FileRepository;
import com.cmpe281.project1.config.BucketName;
import com.cmpe281.project1.entity.Files;
import lombok.AllArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
@AllArgsConstructor
public class FileServiceApply implements FileService {
    private final FileOperation fileOperation;
    private final FileRepository repository;

    @Override
    public Files uploadFile(String username, String title, String description, MultipartFile file) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is greater than 10 MB
        if (file.getSize() > 10485760) {
            throw new IllegalStateException("File uploaded is greater than 10 MB");
        }
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save File in S3 and then save Files in the database
        String path = String.format("%s/%s", BucketName.FILE.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());
        try {
            fileOperation.upload(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        Files files = Files.builder()
                .description(description)
                .username(username)
                .title(title)
                .path(path)
                .fileName(fileName)
                .build();
        repository.save(files);
        return repository.findByTitle(files.getTitle());
    }

    @Override
    public List<Files> getFiles(String username) {
        List<Files> files = new ArrayList<>();
        repository.findByUsername(username).forEach(files::add);
        return files;
    }

    @Override
    public String getPresignedUrl(Integer id) {
        Files files = repository.findById(id);
        String key = files.getPath().substring(files.getPath().indexOf("/")+1)+"/"+files.getFileName();
        return fileOperation.generatePresignedUrl(key);
    }

    @Override
    public String deleteFile(Integer id) {
        Files files = repository.findById(id);

        fileOperation.delete(files.getPath(),files.getFileName());

        files.builder()
                .id(id)
                .build();
        repository.delete(files);
        return "Sucess";
    }

}