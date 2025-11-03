package com.example.lazyco.backend.core.Utils;

public interface CommonConstrains extends APISchema {
  String HOME_DIR = System.getProperty("catalina.home");
  String TEMP_DIR = System.getProperty("java.io.tmpdir");
  // Tomcat home directory
  String TOMCAT_HOME = HOME_DIR.concat("/"); // Tomcat home directory
  String TOMCAT_TEMP = TEMP_DIR.concat("/"); // Tomcat temp directory

  // File Storage Locations
  String UPLOADS_LOCATION = TOMCAT_HOME.concat("uploads/");

  String BATCH_AUDIT_UPLOAD_LOCATION = UPLOADS_LOCATION.concat("batch_audit/");
}
