package com.cmpe281.project1;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestServices {

    @PostMapping("/login")
    String login(@RequestParam String username, @RequestParam String password) throws IOException {

        String AWS_KEY= "AKIA2YMERL6T2E3CSVEQ";
        String AWS_SECRET= "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";
        String REGION= "us-east-2";
        String CLIENT_ID = "223mvl3br3n1gfdkottm7646mg";
        String POOL_ID = "us-east-2_WDGEmKsVe";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(AWS_KEY,
                AWS_SECRET);

        CognitoIdentityProviderClient identityProviderClient =
                CognitoIdentityProviderClient.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                        .region(Region.of(REGION))
                        .build();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", user.getUsername());
        authParams.put("PASSWORD", user.getPassword());

        final AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .clientId(CLIENT_ID)
                .userPoolId(POOL_ID)
                .authParameters(authParams)
                .build();

        try {
            AdminInitiateAuthResponse result = identityProviderClient.adminInitiateAuth(authRequest);
            return(result.authenticationResult().accessToken());
        } catch (Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
    }

    @PostMapping("/signup")
    String signup(@RequestParam String username, @RequestParam String password, @RequestParam String name, @RequestParam String given_name) throws IOException {

        String REGION= "us-east-2";
        String CLIENT_ID = "223mvl3br3n1gfdkottm7646mg";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setGiven_name(given_name);

        List <AttributeType > userAttributes = new ArrayList <AttributeType >();
        AttributeType customAttributeType = new AttributeType().withName("name").withValue(user.getUsername());
        userAttributes.add(customAttributeType);
        customAttributeType = new AttributeType().withName("given_name").withValue(user.getGiven_name());
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
        signUpRequest.setPassword(user.getPassword());
        signUpRequest.setUserAttributes(userAttributes);

        try {
            SignUpResult result = cognitoIdentityProvider.signUp(signUpRequest);
            return "Success";
        } catch (Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
    }

    @GetMapping("/test")
    String test() throws IOException {
        return "Hello";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class GenericException extends RuntimeException {
    }
}
