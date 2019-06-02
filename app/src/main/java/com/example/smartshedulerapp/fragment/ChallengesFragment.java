package com.example.smartshedulerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.ChallengeApiService;
import com.example.smartshedulerapp.di_config.component.ChallengeComponent;
import com.example.smartshedulerapp.di_config.component.DaggerChallengeComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import javax.inject.Inject;

public class ChallengesFragment extends Fragment {

  @Inject
  ChallengeApiService challengeApiService;

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

    ChallengeComponent challengeComponent = DaggerChallengeComponent.builder().appModule(new AppModule(getContext())).build();
    challengeComponent.inject(this);

    return inflate;
  }
}
