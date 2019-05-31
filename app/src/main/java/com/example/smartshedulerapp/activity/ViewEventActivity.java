package com.example.smartshedulerapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
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
import com.example.smartshedulerapp.model.EventDTO;
import com.example.smartshedulerapp.model.EventLocation;
import com.example.smartshedulerapp.model.EventMemberDTO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewEventActivity extends AppCompatActivity implements OnMapReadyCallback {

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
          locationLabel.setText(location.getCountry() + ", " + location.getCity() + ", " + location.getStreet());
          descriptionText.setText(currentEvent.getDescription());
          eventTimeLabel.setText(currentEvent.getStartDate() + " - " + currentEvent.getEndDate());
          categoryLabel.setText("Category: " + currentEvent.getEventCategory());

          memberAdapter = new MemberAdapter(getApplicationContext(), currentEvent.getMemberDTOList());
          membersListView.setAdapter(memberAdapter);

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

}
