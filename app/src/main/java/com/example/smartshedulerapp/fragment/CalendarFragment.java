package com.example.smartshedulerapp.fragment;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.CreateEventFirstStepActivity;
import com.example.smartshedulerapp.adapter.EventPreviewAdapter;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.EventPreviewDTO;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar.CalendarListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

  private static final int REQUEST_EVENT_CREATED = 0;

  @BindView(R.id.calendarView)
  CollapsibleCalendar calendarView;

  @Inject
  EventApiService eventApiService;

  String lastId;

  @BindView(R.id.eventListRecycleView)
  RecyclerView eventListRecycleView;

  RecyclerView.Adapter adapter;

  ProgressDialog progressDialog;

  List<EventPreviewDTO> eventPreviewList = new ArrayList<>();

  public CalendarFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_calendar, container, false);
    ButterKnife.bind(this, view);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(getContext())).build();
    eventComponent.inject(this);

    progressDialog = new ProgressDialog(getContext());
    eventListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    adapter = new EventPreviewAdapter(getContext());
    eventListRecycleView.setAdapter(adapter);
    ((EventPreviewAdapter) adapter).setItemModelList(eventPreviewList);

    fetchDaysWithEvents(LocalDate.now().getMonth());

    calendarView.expand(0);
    fetchSelectedDayEvents(calendarView.getSelectedDay());

    configureCalendarListener();

    return view;
  }

  private void configureCalendarListener() {
    calendarView.setCalendarListener(new CalendarListener() {
      @Override
      public void onDaySelect() {

        progressDialog.setMessage("Loading events...");
        progressDialog.show();

        Day day = calendarView.getSelectedDay();

        fetchSelectedDayEvents(day);
      }

      @Override
      public void onItemClick(View v) {

      }

      @Override
      public void onDataUpdate() {

      }

      @Override
      public void onMonthChange() {

      }

      @Override
      public void onWeekChange(int position) {

      }
    });
  }

  private void fetchDaysWithEvents(Month month) {

    eventApiService.getDaysWithEvents(month).enqueue(new Callback<List<LocalDate>>() {

      @Override
      public void onResponse(Call<List<LocalDate>> call, Response<List<LocalDate>> response) {
        if (response.isSuccessful()) {

          List<LocalDate> previewList = response.body();

          if (!previewList.isEmpty()) {

            previewList.forEach(eventDate -> calendarView.addEventTag(eventDate.getYear(), eventDate.getMonthValue() - 1, eventDate.getDayOfMonth()));
          }
        }

      }

      @Override
      public void onFailure(Call<List<LocalDate>> call, Throwable t) {
        Log.e("", t.getMessage());
      }
    });
  }

  private void fetchSelectedDayEvents(Day day) {
    LocalDateTime from = LocalDateTime.of(LocalDate.of(day.getYear(), day.getMonth() + 1, day.getDay()), LocalTime.MIN);

    eventApiService.getUserEventsPreview(from.format(ISO_LOCAL_DATE_TIME), from.withHour(23).withMinute(59).format(ISO_LOCAL_DATE_TIME))
        .enqueue(new Callback<List<EventPreviewDTO>>() {

          @Override
          public void onResponse(Call<List<EventPreviewDTO>> call, Response<List<EventPreviewDTO>> response) {

            progressDialog.dismiss();
            if (response.isSuccessful()) {

              eventPreviewList = response.body();

              if (!eventPreviewList.isEmpty()) {

                lastId = eventPreviewList.get(eventPreviewList.size() - 1).getEventId();
              }

              ((EventPreviewAdapter) adapter).setItemModelList(eventPreviewList);
              adapter.notifyDataSetChanged();
            }

          }

          @Override
          public void onFailure(Call<List<EventPreviewDTO>> call, Throwable t) {
            Toast.makeText(getContext(), "Fail with loading events list", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
          }
        });
  }

  @OnClick(R.id.createEventActionButton)
  public void createEventOnClick() {
    Intent intent = new Intent(getContext(), CreateEventFirstStepActivity.class);
    startActivityForResult(intent, REQUEST_EVENT_CREATED);
  }


}
