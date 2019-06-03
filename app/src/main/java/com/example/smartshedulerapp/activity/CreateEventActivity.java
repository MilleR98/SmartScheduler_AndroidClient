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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.EventLocation;
import com.example.smartshedulerapp.model.type.EventCategory;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import com.example.smartshedulerapp.util.PlacesFieldSelector;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventActivity extends AppCompatActivity implements OnItemSelectedListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  private static final int PLACE_PICKER_REQUEST = 1;

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
  @BindView(R.id.locationInput)
  EditText inputLocation;
  @BindView(R.id.create_event_title)
  TextView createEventTitle;

  private LocalDateTime selectedStartDate;
  private LocalDateTime selectedEndDate;
  private boolean isStartTimeSelecting;
  private PlacesFieldSelector fieldSelector;
  private EventDTO eventDTO;
  private EventLocation eventLocation = new EventLocation();
  private boolean isForUpdate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event);

    ButterKnife.bind(this);

    ArrayAdapter<EventCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventCategory.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(adapter);
    categorySpinner.setOnItemSelectedListener(this);

    Places.initialize(getApplicationContext(), "AIzaSyAE_63RwAK2MUch6KCCiQrRoJDfNvFtE6Q");

    fieldSelector = new PlacesFieldSelector();

    eventReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

      eventDTO.setEnableReminders(isChecked);
    });

    isForUpdate = getIntent().getBooleanExtra("isForEdit", false);

    if (isForUpdate) {

      eventDTO = (EventDTO) getIntent().getSerializableExtra("currentEvent");
      inputEventTitle.setText(eventDTO.getName());
      inputEventDescription.setText(eventDTO.getDescription());
      inputStartDate.setText(selectedStartDate.format(DATE_TIME_FORMATTER));
      inputEndDate.setText(selectedEndDate.format(DATE_TIME_FORMATTER));
      eventReminderSwitch.setSelected(eventDTO.isEnableReminders());
      int spinnerPosition = adapter.getPosition(eventDTO.getEventCategory());
      categorySpinner.setSelection(spinnerPosition);
      createEventTitle.setText("Edit event");
    } else {

      createEventTitle.setText("Create event");
      eventDTO = new EventDTO();
    }
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
    if (resultCode == AutocompleteActivity.RESULT_OK) {

      Place place = Autocomplete.getPlaceFromIntent(intent);

      inputLocation.setText(place.getAddress());

      List<AddressComponent> addressComponents = place
          .getAddressComponents()
          .asList();

      addressComponents.forEach(this::setLocationFields);

      eventLocation.setLatitude(place.getLatLng().latitude);
      eventLocation.setLongitude(place.getLatLng().longitude);

    }

    super.onActivityResult(requestCode, resultCode, intent);
  }

  private void setLocationFields(AddressComponent addressComponent) {
    if (addressComponent.getTypes().contains("street_number")) {

      eventLocation.setBuildingNumber(addressComponent.getShortName());
    } else if (addressComponent.getTypes().contains("route")) {

      eventLocation.setStreet(addressComponent.getShortName());
    } else if (addressComponent.getTypes().contains("locality")) {

      eventLocation.setCity(addressComponent.getShortName());
    } else if (addressComponent.getTypes().contains("country")) {

      eventLocation.setCountry(addressComponent.getShortName());
    }
  }

  @OnClick(R.id.locationInput)
  public void locationInputOnClick() {

    Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldSelector.getAllFields())
        .build(this.getApplicationContext());

    startActivityForResult(autocompleteIntent, PLACE_PICKER_REQUEST);
  }

  @OnClick(R.id.createEventBtn)
  public void createEventSubmitting() {
    EventDTO eventDTO = (EventDTO) getIntent().getSerializableExtra("eventDTO");

    eventDTO.setEventLocation(eventLocation);
    eventDTO.setCurrentUserPermission(EventMemberPermission.OWNER);

    if (isForUpdate) {

      updateEvent(eventDTO);
    } else {

      createEvent(eventDTO);
    }
  }

  private void updateEvent(EventDTO eventDTO) {
    eventApiService.updateEvent(eventDTO.getId(), eventDTO).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {

          Toast.makeText(CreateEventActivity.this, "Event info updated", Toast.LENGTH_LONG).show();
          finish();
        } else {

          Toast.makeText(CreateEventActivity.this, "Fail create event", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(CreateEventActivity.this, "Fail create event", Toast.LENGTH_LONG).show();
      }
    });
  }

  private void createEvent(EventDTO eventDTO) {
    eventApiService.createEvent(eventDTO).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {

          finish();
        } else {

          Toast.makeText(CreateEventActivity.this, "Fail create event", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(CreateEventActivity.this, "Fail create event", Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    if (isStartTimeSelecting) {

      selectedStartDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
    } else {

      selectedEndDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);

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
      inputStartDate.setText(selectedStartDate.format(DATE_TIME_FORMATTER));
    } else {

      selectedEndDate = selectedEndDate.withHour(hourOfDay).withMinute(minute);
      inputEndDate.setText(selectedEndDate.format(DATE_TIME_FORMATTER));
    }

    if (isStartTimeSelecting) {

      eventDTO.setStartDate(selectedStartDate);
    } else {

      eventDTO.setEndDate(selectedEndDate);
    }
  }
}
