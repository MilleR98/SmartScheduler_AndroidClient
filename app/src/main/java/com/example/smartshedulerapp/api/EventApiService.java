package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.Event;
import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.EventMemberDTO;
import com.example.smartshedulerapp.model.EventPreviewDTO;
import java.time.LocalDateTime;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventApiService {

  @GET("/events/previews")
  Call<List<EventPreviewDTO>> getUserEventsPreview(@Query("from") LocalDateTime from, @Query("to") LocalDateTime to);

  @GET("/events/{id}")
  Call<EventDTO> getEventInfo(@Path("id") String eventId);

  @POST("/events/{id}/invite")
  Call<ResponseBody> inviteMemberToEvent(@Path("id") String eventId, @Body EventMemberDTO eventMemberDTO);

  @POST("/events")
  Call<ResponseBody> createEvent(@Body EventDTO eventDTO);

  @PUT("/events/{id}")
  Call<ResponseBody> updateEvent(@Path("id") String eventId, @Body Event eventDTO);

  @DELETE("/events/{id}")
  Call<ResponseBody> removeEvent(@Path("id") String eventId);
}