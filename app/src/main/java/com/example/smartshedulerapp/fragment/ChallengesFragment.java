package com.example.smartshedulerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.smartshedulerapp.R;

public class ChallengesFragment extends Fragment {

  public ChallengesFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    ChallengesFragment challengesFragment = this;
    View inflate = inflater.inflate(R.layout.fragment_challenges, container, false);


    return inflate;
  }
}