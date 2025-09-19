package com.example.lazyco.backend.core.Utils;

public interface APISchema {

  // Base package for the backend application
  String BACKEND_PACKAGE = "com.example.lazyco.backend";

  // API Path
  String API_PREFIX = "";
  String API_VERSION = "";
  String API_BASE_PATH = API_PREFIX + API_VERSION;

  // Specific API Endpoints
  String APP_USER_API = API_BASE_PATH + "/app_user";
}
