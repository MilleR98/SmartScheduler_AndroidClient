package com.example.smartshedulerapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.CreateEventFirstStepActivity;

public class CalendarFragment extends Fragment {

  private static final int REQUEST_EVENT_CREATED = 0;

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

    return view;
  }

  @OnClick(R.id.createEventActionButton)
  public void createEventOnClick() {
    Intent intent = new Intent(getContext(), CreateEventFirstStepActivity.class);
    startActivityForResult(intent, REQUEST_EVENT_CREATED);
  }
}
