package com.example.smartshedulerapp.model;

import lombok.Data;

@Data
public class SignUpDTO {

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String password;
  private String repeatPassword;
}
