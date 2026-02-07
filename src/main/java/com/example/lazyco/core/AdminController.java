package com.example.lazyco.core;

import com.example.lazyco.core.Cache.TimedECacheLRU;
import com.example.lazyco.core.DateUtils.DateTimeZoneUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final SessionFactory sessionFactory;
  private final JdbcTemplate jdbcTemplate;

  public AdminController(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
    this.sessionFactory = sessionFactory;
    this.jdbcTemplate = jdbcTemplate;
  }

  @PostMapping("/cache/clear")
  public Map<String, Object> clearSystemCache() {
    Map<String, Object> response = new LinkedHashMap<>();
    Map<String, Object> cacheResults = new LinkedHashMap<>();

    boolean success = true;

    // Clear Hibernate second-level cache
    try {
      sessionFactory.getCache().evictAllRegions();
      cacheResults.put(
          "hibernate", "Hibernate second-level cache cleared successfully");
    } catch (Exception e) {
      success = false;
      cacheResults.put("hibernate", Map.of("status", "failed", "message", e.getMessage()));
    }

    // Clear custom application caches
    try {
      TimedECacheLRU.clearAll();
      cacheResults.put(
          "eCache","ECache (TimedECacheLRU) cleared successfully");
    } catch (Exception e) {
      success = false;
      cacheResults.put("eCache", Map.of("status", "failed", "message", e.getMessage()));
    }

    response.put(
        "message",
        success ? "All caches cleared successfully" : "One or more caches failed to clear");
    response.put("caches", cacheResults);
    response.put(
        "note",
        "This will reset all cached data. Use this endpoint when you want to ensure that the latest data is fetched from the database after changes.");
    response.put(
        "warning",
        "This endpoint is intended for development and debugging purposes. Use with caution in production environments.");
    response.put("timestamp", DateTimeZoneUtils.getCurrentDate());
    return response;
  }

  @PostMapping("/sequences/reset")
  public Map<String, Object> resetHibernateSequences() {
    Map<String, Object> response = new LinkedHashMap<>();
    Map<String, Object> results = new LinkedHashMap<>();

    boolean success = true;

    try {
      List<String> sequenceNames =
          jdbcTemplate.queryForList("SELECT sequence_name FROM hibernate_sequences", String.class);

      for (String table : sequenceNames) {
        try {
          Long maxId =
              jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) FROM " + table, Long.class);

          jdbcTemplate.update(
              "UPDATE hibernate_sequences SET next_hi = ? WHERE sequence_name = ?", maxId, table);

          results.put(table, maxId);
        } catch (Exception e) {
          success = false;
          results.put(table, Map.of("status", "failed", "error", e.getMessage()));
        }
      }
    } catch (Exception e) {
      success = false;
      response.put("error", e.getMessage());
    }

    response.put(
        "message",
        success
            ? "Hibernate sequences reset successfully"
            : "One or more sequences failed to reset");
    response.put("sequences", results);
    response.put(
        "note",
        "This operation will reset the Hibernate sequence values to match the current maximum ID values in the database. Use this endpoint after performing database operations that may have caused sequence values to become out of sync, such as manual inserts or deletions.");
    response.put(
        "warning",
        "This endpoint is intended for development and debugging purposes. Use with caution in production environments, as resetting sequences can lead to ID conflicts if not done carefully.");
    response.put("timestamp", DateTimeZoneUtils.getCurrentDate());
    return response;
  }
}
