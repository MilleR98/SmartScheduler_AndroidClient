package com.example.smartshedulerapp.model;

import com.miller.smartscheduler.model.type.EventMemberPermission;
import lombok.Data;

@Data
public class EventMember {

  private String id;
  private String userId;
  private String eventId;
  private boolean isAccepted;
  private EventMemberPermission eventMemberPermission;
}
