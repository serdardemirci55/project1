package com.cmpe281.project1.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Serdar Demirci
 */
@AllArgsConstructor
@Getter
public enum CloudFrontName {
    FILE("d3h00ca9xttt56.cloudfront.net");
    private final String cloudFrontName;
}