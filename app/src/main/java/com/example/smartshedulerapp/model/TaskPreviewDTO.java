package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.SubtaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String id;
  private String title;
  private LocalDateTime deadlineDate;
  private LocalDateTime createdAt;
  private List<SubtaskStatus> subtaskStatuses;
}
