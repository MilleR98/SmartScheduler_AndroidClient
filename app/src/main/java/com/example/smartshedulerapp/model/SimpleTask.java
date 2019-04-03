package com.example.smartshedulerapp.model;

import com.miller.smartscheduler.model.type.ReminderType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SimpleTask {

  private String id;
  private String name;
  private ReminderType reminderType;
  private LocalDateTime reminderTime;
  private LocalDateTime deadlineDate;
  private String userId;
}
