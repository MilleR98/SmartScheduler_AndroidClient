package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.NotificationType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Notification {

  private String id;
  private String userId;
  private String content;
  private Boolean seen;
  private LocalDateTime createdAt;
  private NotificationType notificationType;
}