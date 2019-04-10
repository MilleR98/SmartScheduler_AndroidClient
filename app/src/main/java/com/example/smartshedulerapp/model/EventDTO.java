package com.example.smartshedulerapp.model;

import com.example.smartshedulerapp.model.type.EventCategory;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class EventDTO implements Serializable {

  private String id;
  private String title;
  private String startDate;
  private String endDate;
  private String description;
  private EventCategory eventCategory;
  private EventLocation eventLocation;
  private EventMemberPermission currentUserPermission;
  private List<EventMemberDTO> memberDTOList;
}
