package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.EventMemberPermission;
import lombok.Data;

@Data
public class EventMemberDTO {

  private String userId;
  private String firstName;
  private String lastName;
  private String memberEmail;
  private EventMemberPermission memberPermission;
}
