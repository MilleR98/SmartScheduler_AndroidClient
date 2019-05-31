package com.example.smartshedulerapp.activity;

import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.type.EventCategory;
import java.time.LocalDateTime;
import java.util.Calendar;
import javax.inject.Inject;

public class CreateEventFirstStepActivity extends AppCompatActivity implements OnItemSelectedListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  @Inject
  EventApiService eventApiService;
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
  @BindView(R.id.eventReminderSwitch)
  Switch eventReminderSwitch;
  private LocalDateTime selectedStartDate;
  private LocalDateTime selectedEndDate;
  private boolean isStartTimeSelecting;
  private boolean enableReminders;
  private EventDTO eventDTO = new EventDTO();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event);

    ButterKnife.bind(this);

    ArrayAdapter<EventCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventCategory.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(adapter);
    categorySpinner.setOnItemSelectedListener(this);

    eventReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
      enableReminders = isChecked;
    });
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

    eventDTO.setName(inputEventTitle.getText().toString());
    eventDTO.setDescription(inputEventDescription.getText().toString());
    eventDTO.setEnableReminders(enableReminders);

    Intent intent = new Intent(this, CreateEventSecondStepActivity.class);
    intent.putExtra("eventDTO", eventDTO);

    startActivity(intent);
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    if (isStartTimeSelecting) {

      selectedStartDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
      inputStartDate.setText(selectedStartDate.format(DATE_TIME_FORMATTER));
    } else {

      selectedEndDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
      inputEndDate.setText(selectedEndDate.format(DATE_TIME_FORMATTER));
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

      selectedStartDate = selectedStartDate.withHour(hourOfDay).withMinute(minute);
    } else {

      selectedEndDate = selectedEndDate.withHour(hourOfDay).withMinute(minute);
    }

    if (isStartTimeSelecting) {

      eventDTO.setStartDate(selectedStartDate);
    } else {

      eventDTO.setEndDate(selectedEndDate);
    }
  }
}
