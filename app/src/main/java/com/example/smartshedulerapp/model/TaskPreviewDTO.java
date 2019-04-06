package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.ReminderType;
import com.example.smartshedulerapp.model.type.SubtaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String name;
  private ReminderType reminderType;
  private LocalDateTime deadlineDate;
  private List<SubtaskStatus> subtaskStatuses;
}
