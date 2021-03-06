package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.EventCategory;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Event {

  private String id;
  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String description;
  private EventCategory eventCategory;
  private String locationId;
}
