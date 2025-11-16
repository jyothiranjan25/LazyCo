package com.example.lazyco.backend.core.Utils;

public interface CommonConstants {

  // Base package for the backend application
  String BACKEND_PACKAGE = "com.example.lazyco.backend";
  String APPLICATION_PROPERTIES = "application.properties";

  // Environment Variables
  String TEST_MODE = "Test";
  String DEV_MODE = "Development";
  String PROD_MODE = "Production";

  // System Properties
  String HOME_DIR = System.getProperty("catalina.home");
  String TEMP_DIR = System.getProperty("java.io.tmpdir");
  // Tomcat home directory
  String TOMCAT_HOME = HOME_DIR.concat("/"); // Tomcat home directory
  String TOMCAT_TEMP = TEMP_DIR.concat("/"); // Tomcat temp directory

  // File Storage Locations
  String UPLOADS_LOCATION = "uploads/";
  String BATCH_AUDIT_UPLOAD_LOCATION = UPLOADS_LOCATION.concat("batch_audit/");

  // logged-in user constants
  String LOGGED_USER = "logged_user_id";
  String LOGGED_USER_ROLE = "logged_user_role_id";

  // batch jon constants
  String BATCH_JOB_ID = "batch_job_id";
  String BATCH_JOB_NAME = "batch_job_name";
  String BATCH_JOB_FILE_PATH = "batch_job_file_path";
  String Batch_JOB_TIME_STAMP = "batch_job_time_stamp";
}
