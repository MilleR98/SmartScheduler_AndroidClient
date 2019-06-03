package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.EventMemberDTO;
import com.example.smartshedulerapp.model.EventPreviewDTO;
import java.time.LocalDate;
import java.time.Month;
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
  Call<List<EventPreviewDTO>> getUserEventsPreview(@Query("from") String from, @Query("to") String to);

  @GET("/events/day-markers")
  Call<List<LocalDate>> getDaysWithEvents(@Query("month") Month month);

  @GET("/events/{id}")
  Call<EventDTO> getEventInfo(@Path("id") String eventId);

  @POST("/events/{id}/members/invite")
  Call<ResponseBody> inviteMemberToEvent(@Path("id") String eventId, @Body EventMemberDTO eventMemberDTO);

  @DELETE("/events/{eventId}/members/{memberId}")
  Call<ResponseBody> deleteMemberFromEvent(@Path("eventId") String eventId, @Path("memberId") String memberId);

  @POST("/events")
  Call<ResponseBody> createEvent(@Body EventDTO eventDTO);

  @PUT("/events/{id}")
  Call<ResponseBody> updateEvent(@Path("id") String eventId, @Body EventDTO eventDTO);

  @DELETE("/events/{id}")
  Call<ResponseBody> removeEvent(@Path("id") String eventId);
}
