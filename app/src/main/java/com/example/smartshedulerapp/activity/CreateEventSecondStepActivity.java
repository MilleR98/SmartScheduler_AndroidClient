package com.example.smartshedulerapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;

public class CreateEventSecondStepActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event_second_step);

    ButterKnife.bind(this);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    eventComponent.inject(this);
  }
}
