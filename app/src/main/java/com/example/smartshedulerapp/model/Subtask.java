package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.SubtaskPriority;
import com.example.smartshedulerapp.model.type.SubtaskStatus;
import lombok.Data;

@Data
public class Subtask {

  private String id;
  private String name;
  private SubtaskPriority priority;
  private SubtaskStatus subtaskStatus;
  private String taskId;
}
