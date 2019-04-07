package com.example.smartshedulerapp.model;

import lombok.Data;

@Data
public class TokenPair {

  private String authToken;
  private String refreshToken;
}
