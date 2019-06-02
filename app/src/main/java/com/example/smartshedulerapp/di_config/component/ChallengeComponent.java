package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.api.ChallengeApiService;
import com.example.smartshedulerapp.di_config.module.ChallengeApiModule;
import com.example.smartshedulerapp.fragment.ChallengesFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = ChallengeApiModule.class)
public interface ChallengeComponent {

  ChallengeApiService getChallengeApiService();

  void inject(ChallengesFragment challengesFragment);
}
