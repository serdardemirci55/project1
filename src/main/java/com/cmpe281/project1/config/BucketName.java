package com.cmpe281.project1.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Serdar Demirci
 */
@AllArgsConstructor
@Getter
public enum BucketName {
    FILE("cmpe281project1bucket1");
    private final String bucketName;
}