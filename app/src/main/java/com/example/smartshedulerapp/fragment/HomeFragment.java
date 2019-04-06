package com.example.smartshedulerapp.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;

public class HomeFragment extends Fragment {

  @BindView(R.id.switchTaskBtn)
  Button switchTaskBtn;

  @BindView(R.id.switchEventsBtn)
  Button switchEventsBtn;

  private boolean isTaskFragment = true;
  private TasksListFragment tasksListFragment = new TasksListFragment();
  private DailyEventsFragment dailyEventsFragment = new DailyEventsFragment();


  public HomeFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, view);

    Button switchTaskBtn = this.switchTaskBtn;
    setSwitchBtnEnabled(switchTaskBtn);
    setSwitchBtnDisabled(switchEventsBtn);

    loadFragment(tasksListFragment);

    return view;
  }

  private void setSwitchBtnDisabled(Button switchBtn) {
    GradientDrawable gradientDrawableForEventsBtn = (GradientDrawable) switchBtn.getBackground().mutate();
    gradientDrawableForEventsBtn.setColor(Color.TRANSPARENT);
    switchBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
  }

  private void setSwitchBtnEnabled(Button switchBtn) {
    GradientDrawable gradientDrawableForTasksBtn = (GradientDrawable) switchBtn.getBackground().mutate();
    gradientDrawableForTasksBtn.setColor(getResources().getColor(R.color.colorPrimaryDark));
    switchBtn.setTextColor(Color.WHITE);
  }

  private void loadFragment(Fragment fragment) {

    getActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.home_fragment_container, fragment)
        .disallowAddToBackStack()
        .addToBackStack(null)
        .commit();
  }

  @OnClick(R.id.switchTaskBtn)
  public void showTasksListFragment() {
    if (!isTaskFragment) {
      setSwitchBtnEnabled(switchTaskBtn);
      setSwitchBtnDisabled(switchEventsBtn);
      isTaskFragment = true;
      loadFragment(tasksListFragment);
    }
  }

  @OnClick(R.id.switchEventsBtn)
  public void showEventsListFragment() {
    if (isTaskFragment) {
      setSwitchBtnEnabled(switchEventsBtn);
      setSwitchBtnDisabled(switchTaskBtn);
      isTaskFragment = false;
      loadFragment(dailyEventsFragment);
    }
  }


}
