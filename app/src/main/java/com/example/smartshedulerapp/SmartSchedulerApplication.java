package com.example.smartshedulerapp;

import static com.example.smartshedulerapp.util.Constants.REPORT_PASSWORD;
import static com.example.smartshedulerapp.util.Constants.REPORT_URL;
import static com.example.smartshedulerapp.util.Constants.REPORT_USERNAME;

import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraHttpSender;
import org.acra.annotation.AcraToast;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

@AcraCore(buildConfigClass = BuildConfig.class,
    reportFormat = StringFormat.JSON)
@AcraHttpSender(uri = REPORT_URL,
    httpMethod = HttpSender.Method.POST,
    basicAuthLogin = REPORT_USERNAME,
    basicAuthPassword = REPORT_PASSWORD)
@AcraToast(resText = R.string.crash_report_message)
public class SmartSchedulerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ACRA.init(this);
  }
}
