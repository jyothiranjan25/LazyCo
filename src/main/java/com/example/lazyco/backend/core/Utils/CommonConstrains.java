package com.example.lazyco.backend.core.Utils;

public interface CommonConstrains extends APISchema {
  // Tomcat home directory
  String TOMCAT_HOME = System.getProperty("catalina.home").concat("/"); // Tomcat home directory
  String TOMCAT_TEMP = System.getProperty("java.io.tmpdir").concat("/"); // Tomcat temp directory

  // File Storage Locations
  String UPLOADS_LOCATION = TOMCAT_HOME.concat("Uploads/");
}
