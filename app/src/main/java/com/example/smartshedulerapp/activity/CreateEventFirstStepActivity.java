package com.example.smartshedulerapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.type.EventCategory;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;

public class CreateEventFirstStepActivity extends AppCompatActivity implements OnItemSelectedListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  @Inject
  EventApiService eventApiService;

  private Date selectedStartDate;
  private Date selectedEndDate;
  private boolean isStartTimeSelecting;

  private EventDTO eventDTO = new EventDTO();

  @BindView(R.id.inputEventTitle)
  EditText inputEventTitle;
  @BindView(R.id.inputEventDescription)
  EditText inputEventDescription;
  @BindView(R.id.inputStartDate)
  EditText inputStartDate;
  @BindView(R.id.inputEndDate)
  EditText inputEndDate;
  @BindView(R.id.categorySpinner)
  Spinner categorySpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event);

    ButterKnife.bind(this);

    ArrayAdapter<EventCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventCategory.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(adapter);
    categorySpinner.setOnItemSelectedListener(this);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    eventDTO.setEventCategory((EventCategory) parent.getItemAtPosition(position));
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  @OnClick(R.id.inputStartDate)
  public void inputStartDateClick() {
    isStartTimeSelecting = true;

    openDatePickerDialog();
  }

  @OnClick(R.id.inputEndDate)
  public void inputEndDateClick() {
    isStartTimeSelecting = false;

    openDatePickerDialog();
  }

  private void openDatePickerDialog() {
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);

    DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, this, year, month, day);
    datePickerDialog.show();
  }

  @OnClick(R.id.backToCalendarIcon)
  public void backToCalendarView() {
    finish();
  }

  @OnClick(R.id.moveToNextStep)
  public void moveToNextStep() {

    eventDTO.setTitle(inputEventTitle.getText().toString());
    eventDTO.setDescription(inputEventDescription.getText().toString());

    Intent intent = new Intent(this, CreateEventSecondStepActivity.class);
    intent.putExtra("eventDTO", eventDTO);

    startActivity(intent);
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    if (isStartTimeSelecting) {

      selectedStartDate = new Date(year, month, dayOfMonth);
    } else {

      selectedEndDate = new Date(year, month, dayOfMonth);
    }

    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme, this, hour, minute, true);
    timePickerDialog.show();
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    if (isStartTimeSelecting) {

      selectedStartDate.setHours(hourOfDay);
      selectedStartDate.setMinutes(minute);
    } else {

      selectedEndDate.setHours(hourOfDay);
      selectedEndDate.setMinutes(minute);
    }

    Calendar instance = Calendar.getInstance();

    if (isStartTimeSelecting) {

      instance.set(selectedStartDate.getYear(), selectedStartDate.getMonth(), selectedStartDate.getDay(), hourOfDay, minute);
      inputStartDate.setText(DateFormat.format("yyyy-MM-dd HH:mm", instance));
      eventDTO.setStartDate(DateFormat.format("yyyy-MM-ddTHH:mm", instance).toString());
    } else {

      instance.set(selectedEndDate.getYear(), selectedEndDate.getMonth(), selectedEndDate.getDay(), hourOfDay, minute);
      inputEndDate.setText(DateFormat.format("yyyy-MM-dd HH:mm", instance));
      eventDTO.setStartDate(DateFormat.format("yyyy-MM-ddTHH:mm", instance).toString());
    }
  }
}
