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
  String UPLOADS_LOCATION = TOMCAT_HOME.concat("uploads/");

  String BATCH_AUDIT_UPLOAD_LOCATION = UPLOADS_LOCATION.concat("batch_audit/");

  // Add more common constraints as needed
  String LOGGED_USER = "logged_user_id";
  String LOGGED_USER_ROLE = "logged_user_role_id";
}
