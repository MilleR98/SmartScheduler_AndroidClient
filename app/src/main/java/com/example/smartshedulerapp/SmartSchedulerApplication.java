package com.example.smartshedulerapp;

import android.app.Application;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

public class SmartSchedulerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);
  }

  public void handleUncaughtException(Thread thread, Throwable e) {
    String stackTrace = Log.getStackTraceString(e);
    String message = e.getMessage();

    System.getProperty("os.version"); // OS version
    String sdk = VERSION.SDK;// API Level
    String device = Build.DEVICE;// Device
    String model = Build.MODEL;// Model
    String product = Build.PRODUCT;

    String content = message + "\nSdk: " + sdk + "\nDevice: " + device + "\nModel: " + model + "\nProduct: " + product + "\n" + stackTrace;


  }
}
