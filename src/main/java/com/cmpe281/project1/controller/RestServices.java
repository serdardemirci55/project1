package com.cmpe281.project1.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.cmpe281.project1.authorization.JwtTokenProvider;
import com.cmpe281.project1.entity.Files;
import com.cmpe281.project1.entity.Login;
import com.cmpe281.project1.entity.UserFileDto;
import com.cmpe281.project1.entity.Users;
import com.cmpe281.project1.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class RestServices {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FileService fileService;

    @PostMapping("/login")
    ResponseEntity<Login> login(@RequestParam String username, @RequestParam String password) throws IOException {
        return new ResponseEntity<>(fileService.login(username, password), HttpStatus.OK);
    }

    @PostMapping("/signup")
    String signup(@RequestParam String username, @RequestParam String password, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String role) throws IOException {

        String REGION= "us-east-2";
        String CLIENT_ID = "1cr20qhha6c4vk39ncna6519h0";

        Users user = new Users();
        user.setUsername(username);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setRole(role);

        List <AttributeType > userAttributes = new ArrayList <AttributeType >();
        AttributeType customAttributeType = new AttributeType().withName("name").withValue(user.getFirst_name());
        userAttributes.add(customAttributeType);
        customAttributeType = new AttributeType().withName("family_name").withValue(user.getLast_name());
        userAttributes.add(customAttributeType);

        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.fromName(REGION))
                .build();

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setClientId(CLIENT_ID);
        signUpRequest.setUsername(user.getUsername());
        signUpRequest.setPassword(password);
        signUpRequest.setUserAttributes(userAttributes);

        try {
            SignUpResult result = cognitoIdentityProvider.signUp(signUpRequest);
            String sql = "INSERT INTO users (username, first_name, last_name, role) VALUES (?, ?, ?, ?)";
            int result_insert = jdbcTemplate.update(sql, user.getUsername(), user.getFirst_name(), user.getLast_name(), user.getRole());
            return "Success";
        } catch (Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
    }

    @PostMapping("/file")
    public ResponseEntity<Files> uploadFile(@RequestParam("username") String username,
                                      @RequestParam("title") String title,
                                      @RequestParam("description") String description,
                                      @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(fileService.uploadFile(username, title, description, file), HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<List<UserFileDto>> getFiles(@RequestHeader (name = "Authorization") String token) {
        return new ResponseEntity<>(fileService.getFiles(new JwtTokenProvider().getUsername("{"+token.substring(7)+"}")), HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public String deleteFile(@RequestParam("id") Integer id) {
        return fileService.deleteFile(id);
    }

    @PostMapping("/updatefile")
    public ResponseEntity<Files> updateFile(@RequestParam("id") Integer id,
                                            @RequestParam("description") String description,
                                            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(fileService.updateFile(id, description, file), HttpStatus.OK);
    }

    @GetMapping("/url")
    public String getUrl(@RequestParam("id") Integer id) {
        return fileService.getPresignedUrl(id);
    }

    @GetMapping("/healthcheck")
    public String healthcheck() { return null; }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class GenericException extends RuntimeException {
    }
}
