package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.SubtaskStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String titile;
  private Date deadlineDate;
  private Date createdAt;
  private List<SubtaskStatus> subtaskStatuses = new ArrayList<>();
}
