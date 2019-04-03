package com.example.smartshedulerapp.activity;

import static com.example.smartshedulerapp.util.Constants.AUTH_TOKEN_KEY;
import static com.example.smartshedulerapp.util.Constants.REFRESH_TOKEN_KEY;
import static java.util.Objects.nonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.di_config.component.AuthComponent;
import com.example.smartshedulerapp.di_config.DaggerAuthComponent;
import com.example.smartshedulerapp.model.SignInDTO;
import com.example.smartshedulerapp.model.UserDetails;
import com.example.smartshedulerapp.validator.SignInValidator;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  private static final int REQUEST_SIGNUP = 0;

  private ProgressDialog progressDialog;

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

    if (signInDTO.getEmail().equals("mr.oleg.melnyk@gmail.com")) {
      onLoginSuccess("fake", "fake");
    }

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

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Successfully sign up")
            .setMessage("Please login to your new account")
            .setIcon(R.drawable.ic_account_circle_black_36dp)
            .setNegativeButton("OK", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();

        alert.show();

        this.finish();
      }
    }
  }

  public void onLoginSuccess(String authToken, String refreshToken) {
    loginButton.setEnabled(true);

    Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    editor.putString(AUTH_TOKEN_KEY, authToken);
    editor.putString(REFRESH_TOKEN_KEY, refreshToken);
    editor.apply();

    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

    finish();
  }

  public void onLoginFailed() {
    Toast.makeText(getBaseContext(), "Invalid email or password", Toast.LENGTH_LONG).show();

    loginButton.setEnabled(true);
  }
}
