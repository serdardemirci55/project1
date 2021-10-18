package com.cmpe281.project1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Builder
@Entity
public class Login {
    @Id
    private String token;
    private String role;

    public Login(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
