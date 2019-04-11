package com.example.smartshedulerapp.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventSecondStepActivity extends AppCompatActivity implements InviteMemberDialog.InviteMemberDialogListener {

  @Inject
  EventApiService eventApiService;

  @BindView(R.id.membersListView)
  ListView membersListView;
  @BindView(R.id.inputCountry)
  EditText inputCountry;
  @BindView(R.id.inputCity)
  EditText inputCity;
  @BindView(R.id.inputStreet)
  EditText inputStreet;

  private List<EventMemberDTO> memberList = new ArrayList<>();
  private MemberAdapter memberAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event_second_step);

    ButterKnife.bind(this);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    eventComponent.inject(this);

    memberAdapter = new MemberAdapter(getApplicationContext(), memberList);
    membersListView.setAdapter(memberAdapter);
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

  @OnClick(R.id.submitEventCreating)
  public void createEventSubmitting() {
    EventDTO eventDTO = (EventDTO) getIntent().getSerializableExtra("eventDTO");

    EventLocation eventLocation = new EventLocation();
    eventLocation.setCity(inputCity.getText().toString());
    eventLocation.setCountry(inputCountry.getText().toString());
    eventLocation.setStreet(inputStreet.getText().toString());

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
