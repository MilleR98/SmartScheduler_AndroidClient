package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.SubtaskStatus;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String title;
  private String deadlineDate;
  private String createdAt;
  private List<SubtaskStatus> subtaskStatuses;
}
