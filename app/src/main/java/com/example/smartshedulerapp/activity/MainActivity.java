package com.example.smartshedulerapp.activity;

import static java.util.Objects.nonNull;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.fragment.CalendarFragment;
import com.example.smartshedulerapp.fragment.ChallengesFragment;
import com.example.smartshedulerapp.fragment.DashboardFragment;
import com.example.smartshedulerapp.fragment.HomeFragment;
import com.example.smartshedulerapp.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  private Map<Integer, Runnable> navigationItemActions = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView navigation = findViewById(R.id.navigation);

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = this::processNavigationItemSelection;
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    navigateToHome();

    configureNavigationActionsMap();
  }

  private boolean processNavigationItemSelection(MenuItem item) {

    boolean itemSelected = false;

    if (navigationItemActions.containsKey(item.getItemId())) {

      navigationItemActions.get(item.getItemId()).run();
      itemSelected = true;
    }

    return itemSelected;
  }

  private void configureNavigationActionsMap() {
    navigationItemActions.put(R.id.navigation_home, this::navigateToHome);
    navigationItemActions.put(R.id.navigation_dashboard, this::navigateToDashboard);
    navigationItemActions.put(R.id.navigation_calendar, this::navigateToCalendar);
    navigationItemActions.put(R.id.navigation_achievements, this::navigateToChallenges);
    navigationItemActions.put(R.id.navigation_profile, this::navigateToProfile);
  }

  private void navigateToProfile() {

    loadFragment(new ProfileFragment());
  }

  private void navigateToChallenges() {

    loadFragment(new ChallengesFragment());
  }

  private void navigateToCalendar() {

    loadFragment(new CalendarFragment());
  }

  private void navigateToDashboard() {

    loadFragment(new DashboardFragment());
  }

  private void navigateToHome() {

    loadFragment(new HomeFragment());
  }

  private boolean loadFragment(Fragment fragment) {

    boolean isFragmentChanged = false;

    if (nonNull(fragment)) {

      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_container, fragment)
          .commit();

      isFragmentChanged = true;
    }

    return isFragmentChanged;
  }
}
