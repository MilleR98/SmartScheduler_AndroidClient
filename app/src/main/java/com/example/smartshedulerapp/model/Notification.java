package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.NotificationType;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class Notification {

  private String id;
  private String userId;
  private String content;
  private Map<String, String> additionalParameters;
  private Boolean seen;
  private LocalDateTime createdAt;
  private NotificationType notificationType;
}
