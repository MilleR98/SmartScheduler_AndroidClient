package com.example.smartshedulerapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.ViewEventActivity;
import com.example.smartshedulerapp.adapter.EventPreviewAdapter;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.EventPreviewDTO;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyEventsFragment extends Fragment {

  @Inject
  EventApiService eventApiService;


  String lastId;
  @BindView(R.id.eventListRecycleView)
  RecyclerView eventListRecycleView;

  RecyclerView.Adapter adapter;

  public DailyEventsFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_daily_events, container, false);
    ButterKnife.bind(this, view);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(getContext())).build();
    eventComponent.inject(this);

    ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage("Loading data...");
    progressDialog.show();

    eventListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    eventApiService.getUserEventsPreview("2019-04-06T00:00", "2019-04-06T23:59").enqueue(new Callback<List<EventPreviewDTO>>() {

      @Override
      public void onResponse(Call<List<EventPreviewDTO>> call, Response<List<EventPreviewDTO>> response) {

        progressDialog.dismiss();
        if (response.isSuccessful()) {

          List<EventPreviewDTO> previewList = response.body();

          if (!previewList.isEmpty()) {

            lastId = previewList.get(previewList.size() - 1).getEventId();
            adapter = new EventPreviewAdapter(getContext(), previewList);
            eventListRecycleView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
          }
        }

      }

      @Override
      public void onFailure(Call<List<EventPreviewDTO>> call, Throwable t) {
        Toast.makeText(getContext(), "Fail with loading events list", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
      }
    });

    return view;
  }
}
