package com.cmpe281.project1.entity;

import java.sql.Timestamp;

public interface UserFileDto {
    Integer getId();
    String getFirstName();
    String getLastName();
    String getTitle();
    String getDescription();
    Timestamp getUploadTime();
    Timestamp getUpdatedTime();
}
