package com.cmpe281.project1.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsConfig {
    @Bean
    public AmazonS3 s3() {
        String AWS_KEY= "AKIA2YMERL6TVUGOUUTV";
        String AWS_SECRET= "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";
        String REGION= "us-east-2";

        // Create an Amazon S3 client that is configured to use the accelerate endpoint.
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(AWS_KEY, AWS_SECRET);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .enableAccelerateMode()
                .build();
    }

    @Bean
    public CognitoIdentityProviderClient cognito() {
        String AWS_KEY= "AKIA2YMERL6TVUGOUUTV";
        String AWS_SECRET= "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";
        String REGION= "us-east-2";
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(AWS_KEY,
                AWS_SECRET);
        return CognitoIdentityProviderClient
                        .builder()
                        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                        .region(Region.of(REGION))
                        .build();
    }
}