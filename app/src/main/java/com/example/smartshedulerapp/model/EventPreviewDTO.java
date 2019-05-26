package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.EventMemberPermission;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventPreviewDTO {

  private String eventId;
  private String title;
  private Long membersCount;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private EventMemberPermission currentUserEventPermission;
}