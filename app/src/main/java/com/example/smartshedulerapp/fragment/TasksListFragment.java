package com.example.smartshedulerapp.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartshedulerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {


  public TasksListFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_tasks_list, container, false);
  }

}
