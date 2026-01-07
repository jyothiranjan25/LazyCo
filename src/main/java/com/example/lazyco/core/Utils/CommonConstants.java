package com.example.lazyco.core.Utils;

public interface CommonConstants extends UploadConstants {

  // Base package for the backend application
  String BACKEND_PACKAGE = "com.example.lazyco.backend";
  String APPLICATION_PROPERTIES = "application.properties";

  // Environment Variables
  String TEST_MODE = "Testing";
  String DEV_MODE = "Development";
  String PROD_MODE = "Production";

  // logged-in user constants
  String LOGGED_USER = "logged_user_id";
  String LOGGED_USER_ROLE = "logged_user_role_id";

  // batch jon constants
  String BATCH_JOB_ID = "batch_job_id";
  String BATCH_JOB_NAME = "batch_job_name";
  String BATCH_JOB_FILE_PATH = "batch_job_file_path";
  String Batch_JOB_TIME_STAMP = "batch_job_time_stamp";
}
