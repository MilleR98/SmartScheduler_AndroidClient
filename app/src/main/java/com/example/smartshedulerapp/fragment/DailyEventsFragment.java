package com.example.smartshedulerapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.smartshedulerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyEventsFragment extends Fragment {

  public DailyEventsFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_daily_events, container, false);
  }

}
