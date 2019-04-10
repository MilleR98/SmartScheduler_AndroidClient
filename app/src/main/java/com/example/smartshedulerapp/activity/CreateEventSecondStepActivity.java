package com.example.smartshedulerapp.activity;

import android.os.Bundle;

import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.adapter.MemberAdapter;
import com.example.smartshedulerapp.adapter.SubtaskAdapter;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.dialog.CreateSubtaskDialog;
import com.example.smartshedulerapp.dialog.InviteMemberDialog;
import com.example.smartshedulerapp.dialog.InviteMemberDialog.InviteMemberDialogListener;
import com.example.smartshedulerapp.model.EventMemberDTO;
import com.example.smartshedulerapp.model.Subtask;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class CreateEventSecondStepActivity extends AppCompatActivity implements OnMapReadyCallback, InviteMemberDialog.InviteMemberDialogListener {

  private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
  private GoogleMap gmap;
  private List<EventMemberDTO> memberList = new ArrayList<>();
  private MemberAdapter memberAdapter;

  @BindView(R.id.mapView)
  MapView mapView;

  @BindView(R.id.membersListView)
  ListView membersListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event_second_step);

    ButterKnife.bind(this);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    eventComponent.inject(this);

    Bundle mapViewBundle = null;
    if (savedInstanceState != null) {
      mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
    }

    EventMemberDTO eventMemberDTO = new EventMemberDTO();

    eventMemberDTO.setFirstName("Oleg");
    eventMemberDTO.setLastName("Melnyk");
    eventMemberDTO.setMemberEmail("mr.oleg.melnyk@gmail.com");
    eventMemberDTO.setMemberPermission(EventMemberPermission.VIEWER);
    eventMemberDTO.setCanInviteOthers(true);

    memberList.add(eventMemberDTO);

    memberAdapter = new MemberAdapter(getApplicationContext(), memberList);
    membersListView.setAdapter(memberAdapter);

    mapView.onCreate(mapViewBundle);
    mapView.getMapAsync(this);
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
  public void onMapReady(GoogleMap googleMap) {
    gmap = googleMap;
    gmap.setMinZoomPreference(12);

    UiSettings uiSettings = gmap.getUiSettings();
    uiSettings.setIndoorLevelPickerEnabled(true);
    uiSettings.setMyLocationButtonEnabled(true);
    uiSettings.setMapToolbarEnabled(true);
    uiSettings.setCompassEnabled(true);
    uiSettings.setZoomControlsEnabled(true);

    LatLng ny = new LatLng(40.7143528, -74.0059731);
    gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
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
}
