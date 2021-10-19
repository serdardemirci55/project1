package com.cmpe281.project1.service;

import com.cmpe281.project1.entity.Files;
import com.cmpe281.project1.entity.Login;
import com.cmpe281.project1.entity.UserFileDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Created by Serdar Demirci
 */
public interface FileService {
    Files uploadFile(String username, String title, String description, MultipartFile file);

    List<UserFileDto> getFiles(String username);

    String getPresignedUrl (Integer id);

    String deleteFile (Integer id);

    Files updateFile(Integer id, String description, MultipartFile file);

    Login login(String username, String password);
}