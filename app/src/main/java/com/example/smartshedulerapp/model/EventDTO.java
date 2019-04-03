package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.EventCategory;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class EventDTO {

  private String id;
  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String description;
  private EventCategory eventCategory;
  private EventLocation eventLocation;
  private EventMemberPermission currentUserPermission;
  private List<EventMemberDTO> memberDTOList;
}
