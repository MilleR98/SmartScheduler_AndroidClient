package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.ChallengeStatus;
import lombok.Data;

@Data
public class Challenge {

  private String id;
  private String name;
  private ChallengeStatus status;
  private String startTime;
  private String userId;
}
