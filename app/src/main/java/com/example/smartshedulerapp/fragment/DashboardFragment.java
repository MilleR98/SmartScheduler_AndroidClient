package com.example.smartshedulerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.smartshedulerapp.R;

public class DashboardFragment extends Fragment {

  public DashboardFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_dashboard, container, false);
  }
}