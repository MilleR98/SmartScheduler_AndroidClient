package com.example.smartshedulerapp.fragment;

import static android.content.ContentValues.TAG;
import static com.example.smartshedulerapp.util.Constants.AUTH_TOKEN_KEY;
import static com.example.smartshedulerapp.util.Constants.REFRESH_TOKEN_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.SignInActivity;
import com.example.smartshedulerapp.api.AuthApiService;
import com.example.smartshedulerapp.di_config.component.AuthComponent;
import com.example.smartshedulerapp.di_config.component.DaggerAuthComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.TokenPair;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

  @Inject
  AuthApiService authApiService;

  @BindView(R.id.logoutIcon)
  ImageView logoutIcon;

  @BindView(R.id.settingsIcon)
  ImageView settingsIcon;

  @BindView(R.id.notificationsIcon)
  ImageView notificationIcon;

  public ProfileFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    ButterKnife.bind(this, view);

    AuthComponent authComponent = DaggerAuthComponent.builder()
        .appModule(new AppModule(this.getContext()))
        .build();

    authComponent.inject(this);

    return view;
  }

  @OnClick(R.id.logoutIcon)
  public void onLogoutIconClick() {

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

    TokenPair tokenPair = new TokenPair();
    tokenPair.setAuthToken(sharedPreferences.getString(AUTH_TOKEN_KEY, ""));
    tokenPair.setRefreshToken(sharedPreferences.getString(REFRESH_TOKEN_KEY, ""));

    authApiService.logout(tokenPair).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        Editor editor = sharedPreferences.edit();
        editor.remove(AUTH_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.apply();
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

        Log.e(TAG, "Error logout user", t);
      }
    });

    Intent intent = new Intent(this.getContext(), SignInActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @OnClick(R.id.settingsIcon)
  public void onSettingsIconClick() {

  }

  @OnClick(R.id.notificationsIcon)
  public void onNotificationIconClick() {

  }
}
