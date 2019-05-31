package com.example.smartshedulerapp.activity;

import static com.example.smartshedulerapp.util.Constants.AUTH_TOKEN_KEY;
import static com.example.smartshedulerapp.util.Constants.DEVICE_ID_KEY;
import static com.example.smartshedulerapp.util.Constants.FCM_TOKEN;
import static com.example.smartshedulerapp.util.Constants.REFRESH_TOKEN_KEY;
import static java.util.Objects.nonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.AuthApiService;
import com.example.smartshedulerapp.di_config.component.AuthComponent;
import com.example.smartshedulerapp.di_config.component.DaggerAuthComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.DeviceFcmTokenDTO;
import com.example.smartshedulerapp.model.SignInDTO;
import com.example.smartshedulerapp.model.UserDetails;
import com.example.smartshedulerapp.validator.SignInValidator;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  private static final int REQUEST_SIGNUP = 0;
  @Inject
  AuthApiService authApiService;
  @BindView(R.id.emailInput)
  EditText emailText;
  @BindView(R.id.passwordInput)
  EditText passwordText;
  @BindView(R.id.signInButton)
  Button loginButton;
  @BindView(R.id.linkSignUp)
  TextView signupLink;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);
    ButterKnife.bind(this);

    progressDialog = new ProgressDialog(SignInActivity.this, R.style.Theme_MaterialComponents_Dialog);

    AuthComponent authComponent = DaggerAuthComponent.builder().appModule(new AppModule(this)).build();
    authComponent.inject(this);
  }

  @OnClick(R.id.linkSignUp)
  public void openSignUpPage() {
    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
    startActivityForResult(intent, REQUEST_SIGNUP);
  }

  @OnClick(R.id.signInButton)
  public void login() {
    Log.d(TAG, "Login");

    if (!SignInValidator.validate(emailText, passwordText)) {

      return;
    }

    loginButton.setEnabled(false);

    progressDialog.setIndeterminate(false);
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Authenticating...");
    progressDialog.show();

    SignInDTO signInDTO = new SignInDTO();
    signInDTO.setEmail(emailText.getText().toString());
    signInDTO.setPassword(passwordText.getText().toString());

    authApiService.signIn(signInDTO).enqueue(new Callback<UserDetails>() {

      @Override
      public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {

        if (response.isSuccessful()) {

          String authorizationToken = response.headers().get("Authorization");
          String refreshToken = response.headers().get("Refresh");

          onLoginSuccess(authorizationToken, refreshToken);
        } else {

          onLoginFailed();
        }

        if (nonNull(progressDialog) && progressDialog.isShowing()) {
          progressDialog.dismiss();
        }
      }

      @Override
      public void onFailure(Call<UserDetails> call, Throwable t) {
        progressDialog.dismiss();
        onLoginFailed();
      }
    });
  }


  @Override
  public void onBackPressed() {

    if (nonNull(progressDialog) && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }

    moveTaskToBack(true);
  }

  @Override
  protected void onStop() {
    super.onStop();

    if (nonNull(progressDialog) && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (nonNull(progressDialog) && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_SIGNUP) {
      if (resultCode == RESULT_OK) {

        this.finish();
      }
    }
  }

  public void onLoginSuccess(String authToken, String refreshToken) {
    loginButton.setEnabled(true);

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    Editor editor = sharedPreferences.edit();
    editor.putString(AUTH_TOKEN_KEY, authToken);
    editor.putString(REFRESH_TOKEN_KEY, refreshToken);
    editor.apply();

    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

    DeviceFcmTokenDTO deviceFcmTokenDTO = new DeviceFcmTokenDTO();
    deviceFcmTokenDTO.setDeviceFcmToken(sharedPreferences.getString(FCM_TOKEN, ""));
    deviceFcmTokenDTO.setDeviceId(sharedPreferences.getString(DEVICE_ID_KEY, ""));

    authApiService.registerUserNotificationToken(deviceFcmTokenDTO).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (!response.isSuccessful()) {
          Log.w(TAG, "Device fcm token didnt registered for user");
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

        Log.w(TAG, "Error sending device fcm token", t);
      }
    });

    finish();
  }

  public void onLoginFailed() {
    Toast.makeText(getBaseContext(), "Invalid email or password", Toast.LENGTH_LONG).show();

    loginButton.setEnabled(true);
  }
}
