package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.ReminderType;
import java.util.List;
import lombok.Data;

@Data
public class CreateTaskDTO {

  private String title;
  private String description;
  private ReminderType reminderType;
  private String reminderTime;
  private String deadlineDate;
  private List<Subtask> subtaskList;
}
