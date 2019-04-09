package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.ReminderType;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class CreateTaskDTO {

  private String title;
  private String description;
  private ReminderType reminderType;
  private Date reminderTime;
  private Date deadlineDate;
  private List<Subtask> subtaskList;
}
