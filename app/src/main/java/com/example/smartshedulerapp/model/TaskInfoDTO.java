package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.ReminderType;
import java.util.List;
import lombok.Data;

@Data
public class TaskInfoDTO {

  private String id;
  private String title;
  private String description;
  private ReminderType reminderType;
  private String reminderTime;
  private String deadlineDate;
  private String createdAt;
  private List<Subtask> subtaskList;
}