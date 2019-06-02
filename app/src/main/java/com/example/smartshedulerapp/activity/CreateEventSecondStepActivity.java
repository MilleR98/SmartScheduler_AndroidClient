package com.example.smartshedulerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.adapter.MemberAdapter;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.dialog.InviteMemberDialog;
import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.EventLocation;
import com.example.smartshedulerapp.model.EventMemberDTO;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import com.example.smartshedulerapp.util.PlacesFieldSelector;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventSecondStepActivity extends AppCompatActivity implements InviteMemberDialog.InviteMemberDialogListener {

  private static final int PLACE_PICKER_REQUEST = 1;

  @Inject
  EventApiService eventApiService;

  @BindView(R.id.membersListView)
  ListView membersListView;
  @BindView(R.id.locationInput)
  EditText inputLocation;

  private List<EventMemberDTO> memberList = new ArrayList<>();
  private MemberAdapter memberAdapter;
  private PlacesFieldSelector fieldSelector;
  private EventLocation eventLocation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event_second_step);

    ButterKnife.bind(this);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    eventComponent.inject(this);

    memberAdapter = new MemberAdapter(getApplicationContext());
    memberAdapter.setMembersList(memberList);
    membersListView.setAdapter(memberAdapter);

    Places.initialize(getApplicationContext(), "AIzaSyAE_63RwAK2MUch6KCCiQrRoJDfNvFtE6Q");

    fieldSelector = new PlacesFieldSelector();

    eventLocation = new EventLocation();
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

  @OnClick(R.id.addMemberLayout)
  public void addMemberClick() {
    openInviteMemberDialog();
  }

  public void openInviteMemberDialog() {
    InviteMemberDialog inviteMemberDialog = new InviteMemberDialog();
    inviteMemberDialog.show(getSupportFragmentManager(), "Invite member dialog");
  }

  @Override
  public void applyValues(String firstName, String lastName, String email, EventMemberPermission eventMemberPermission, boolean canInvite) {
    EventMemberDTO eventMemberDTO = new EventMemberDTO();

    eventMemberDTO.setFirstName(firstName);
    eventMemberDTO.setLastName(lastName);
    eventMemberDTO.setMemberEmail(email);
    eventMemberDTO.setMemberPermission(eventMemberPermission);
    eventMemberDTO.setCanInviteOthers(canInvite);

    memberList.add(eventMemberDTO);
    memberAdapter.notifyDataSetChanged();
  }

  @OnClick(R.id.backToFirstStepIcon)
  public void backToCalendarView() {
    finish();
  }

  @OnClick(R.id.locationInput)
  public void locationInputOnClick() {

    Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldSelector.getAllFields())
        .build(this.getApplicationContext());

    startActivityForResult(autocompleteIntent, PLACE_PICKER_REQUEST);
  }

  @OnClick(R.id.submitEventCreating)
  public void createEventSubmitting() {
    EventDTO eventDTO = (EventDTO) getIntent().getSerializableExtra("eventDTO");

    eventDTO.setEventLocation(eventLocation);
    eventDTO.setCurrentUserPermission(EventMemberPermission.OWNER);
    eventDTO.setMemberDTOList(memberList);

    eventApiService.createEvent(eventDTO).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {

          finish();
        } else {

          Toast.makeText(CreateEventSecondStepActivity.this, "Fail create event", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(CreateEventSecondStepActivity.this, "Fail create event", Toast.LENGTH_LONG).show();
      }
    });
  }
}
