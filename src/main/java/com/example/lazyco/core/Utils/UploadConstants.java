package com.example.lazyco.core.Utils;

public interface UploadConstants {
  // System Properties
  String HOME_DIR = System.getProperty("catalina.home");
  String TEMP_DIR = System.getProperty("java.io.tmpdir");
  // Tomcat home directory
  String TOMCAT_HOME = HOME_DIR.concat("/"); // Tomcat home directory
  String TOMCAT_TEMP = TEMP_DIR.concat("/"); // Tomcat temp directory

  // File Storage Locations
  String UPLOADS_LOCATION = "uploads/";
  String BATCH_JOB_UPLOAD_LOCATION = UPLOADS_LOCATION.concat("batch_job/");
}
