package com.example.smartshedulerapp.activity;

    import static com.example.smartshedulerapp.util.Constants.AUTH_TOKEN_KEY;

    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Handler;
    import android.preference.PreferenceManager;
    import androidx.appcompat.app.AppCompatActivity;
    import com.example.smartshedulerapp.R;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.AppTheme);
    setContentView(R.layout.activity_splash);

    new Handler().postDelayed(() -> {

      boolean isLoggedIn = PreferenceManager.getDefaultSharedPreferences(this).contains(AUTH_TOKEN_KEY);

      Intent intent;

      if (isLoggedIn) {

        intent = new Intent(SplashActivity.this, MainActivity.class);
      } else {

        intent = new Intent(SplashActivity.this, SignInActivity.class);
      }

      startActivity(intent);
      finish();
    }, 2000);
  }
}
