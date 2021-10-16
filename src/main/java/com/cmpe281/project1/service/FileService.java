package com.cmpe281.project1.service;

import com.cmpe281.project1.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    Files uploadFile(String username, String title, String description, MultipartFile file);

    List<Files> getFiles(String username);

    String getPresignedUrl (Integer id);

    String deleteFile (Integer id);

    Files updateFile(Integer id, String description, MultipartFile file);
}