package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.ReminderType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskInfoDTO implements Serializable {

  private String id;
  private String title;
  private String description;
  private ReminderType reminderType;
  private LocalDateTime reminderTime;
  private LocalDateTime deadlineDate;
  private String createdAt;
  private List<Subtask> subtaskList;
}
