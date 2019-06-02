package com.example.smartshedulerapp.activity;

import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;
import static java.lang.String.format;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewEventActivity extends AppCompatActivity implements OnMapReadyCallback, InviteMemberDialog.InviteMemberDialogListener {

  private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

  @Inject
  EventApiService eventApiService;

  @BindView(R.id.membersListView)
  ListView membersListView;
  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.locationLabel)
  TextView locationLabel;
  @BindView(R.id.eventTitleLabel)
  TextView eventTitleLabel;
  @BindView(R.id.eventTimeLabel)
  TextView eventTimeLabel;
  @BindView(R.id.description)
  TextView descriptionText;
  @BindView(R.id.categoryLabel)
  TextView categoryLabel;
  @BindView(R.id.inviteMembers)
  Button inviteMembersBtn;
  @BindView(R.id.sendMessages)
  Button sendMessagesBtn;
  @BindView(R.id.membersLabel)
  TextView membersLabel;

  private EventDTO currentEvent;
  private MemberAdapter memberAdapter;

  private GoogleMap gmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_event);

    ButterKnife.bind(this);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    eventComponent.inject(this);

    Bundle mapViewBundle = null;
    if (savedInstanceState != null) {
      mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
    }

    memberAdapter = new MemberAdapter(getApplicationContext());
    memberAdapter.setMembersList(new ArrayList<>());
    membersListView.setAdapter(memberAdapter);

    String eventId = getIntent().getStringExtra("eventId");

    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading event info...");
    progressDialog.show();

    eventApiService.getEventInfo(eventId).enqueue(new Callback<EventDTO>() {

      @Override
      public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {

        progressDialog.dismiss();

        if (response.isSuccessful()) {

          currentEvent = response.body();

          eventTitleLabel.setText(currentEvent.getName());
          EventLocation location = currentEvent.getEventLocation();
          locationLabel.setText(format("%s, %s, %s", location.getCountry(), location.getCity(), location.getStreet()));
          descriptionText.setText(currentEvent.getDescription());
          eventTimeLabel.setText(format("%s - %s", currentEvent.getStartDate().format(DATE_TIME_FORMATTER), currentEvent.getEndDate().format(DATE_TIME_FORMATTER)));
          categoryLabel.setText(format("Category: %s", currentEvent.getEventCategory()));

          if (currentEvent.getMemberDTOList().isEmpty()) {

            membersLabel.setText("No one invited to event... Click below button to invite");
            sendMessagesBtn.setEnabled(false);
          } else {

            membersLabel.setText("Notify all members about something?");
          }

          memberAdapter.setMembersList(currentEvent.getMemberDTOList());
          memberAdapter.notifyDataSetChanged();

          LatLng latLng = new LatLng(currentEvent.getEventLocation().getLatitude(), currentEvent.getEventLocation().getLongitude());

          MarkerOptions markerOptions = new MarkerOptions();
          markerOptions.position(latLng);

          markerOptions.title("Here event's place");

          gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
          gmap.addMarker(markerOptions);

        } else {

          Toast.makeText(getApplicationContext(), "Fail with loading event info", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<EventDTO> call, Throwable t) {

        Toast.makeText(getApplicationContext(), "Fail with loading event info", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
      }
    });

    mapView.onCreate(mapViewBundle);
    mapView.getMapAsync(this);
  }

  @OnClick(R.id.sendMessages)
  public void sendMessages() {
    Object[] membersEmails = currentEvent.getMemberDTOList()
        .stream()
        .map(EventMemberDTO::getMemberEmail)
        .toArray();

    Intent it = new Intent(Intent.ACTION_SEND);
    it.putExtra(Intent.EXTRA_EMAIL, membersEmails);
    it.putExtra(Intent.EXTRA_SUBJECT, currentEvent.getName());
    it.setType("message/rfc822");

    startActivity(Intent.createChooser(it, "Choose Mail App"));
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onPause() {
    mapView.onPause();
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    mapView.onDestroy();
    super.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
    if (mapViewBundle == null) {
      mapViewBundle = new Bundle();
      outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
    }

    mapView.onSaveInstanceState(mapViewBundle);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    gmap = googleMap;
    gmap.setMinZoomPreference(12);

    UiSettings uiSettings = gmap.getUiSettings();
    uiSettings.setIndoorLevelPickerEnabled(true);
    uiSettings.setMyLocationButtonEnabled(true);
    uiSettings.setMapToolbarEnabled(true);
    uiSettings.setCompassEnabled(true);
    uiSettings.setZoomControlsEnabled(true);

    LatLng ny = new LatLng(49.834752, 24.014228);
    gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
  }

  @OnClick(R.id.backToEventList)
  public void onBackToEvents(){
    finish();
  }

  @OnClick(R.id.eventActionsImg)
  public void showPopup(View v) {
    PopupMenu popup = new PopupMenu(this, v);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.event_actions, popup.getMenu());
    popup.show();
  }

  @OnClick(R.id.inviteMembers)
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

    eventApiService.inviteMemberToEvent(currentEvent.getId(), eventMemberDTO).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {
          currentEvent.getMemberDTOList().add(eventMemberDTO);
          memberAdapter.notifyDataSetChanged();

          Toast.makeText(ViewEventActivity.this, eventMemberDTO.getFirstName() + " invited to event", Toast.LENGTH_LONG).show();
          membersLabel.setText("Notify all members about something?");
          sendMessagesBtn.setEnabled(true);
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

      }
    });

  }
}
