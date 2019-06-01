package com.example.smartshedulerapp.util;

import java.time.format.DateTimeFormatter;

public interface Constants {

  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  String AUTH_TOKEN_KEY = "AuthToken";
  String REFRESH_TOKEN_KEY = "RefreshToken";
  String DEVICE_ID_KEY = "DeviceId";
  String FCM_TOKEN = "FcmToken";

  String SERVER_DOMAIN = "https://550eae77.ngrok.io";
  String REPORT_URL = SERVER_DOMAIN + "/error/report";
  String REPORT_USERNAME = "omelnyk";
  String REPORT_PASSWORD = "developer1998";
}