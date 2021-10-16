package com.cmpe281.project1.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AwsConfig {
    @Bean
    public AmazonS3 s3() {
        String AWS_KEY= "AKIA2YMERL6TVUGOUUTV";
        String AWS_SECRET= "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";
        String REGION= "us-east-2";
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(AWS_KEY, AWS_SECRET);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}