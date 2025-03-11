package com.example.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String name;
   private String password;
   private String passwordConfirm;
}
