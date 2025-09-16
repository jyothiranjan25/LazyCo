package com.example.lazyco.backend.core.DateUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeProps {

  // Base date only
  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  // Date + time (space separator)
  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
  public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";

  // ISO-8601 style (T separator)
  public static final String YYYY_MM_DD_T_HH = "yyyy-MM-dd'T'HH";
  public static final String YYYY_MM_DD_T_HH_MM = "yyyy-MM-dd'T'HH:mm";
  public static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSSS = "yyyy-MM-dd'T'HH:mm:ss.SSSS";
  public static final String YYYY_MM_DD_T_HH_MM_SS_XXX = "yyyy-MM-dd'T'HH:mm:ssXXX";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX";

  // UTC/Zulu time support
  public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  // Time only
  public static final String HH_MM = "HH:mm";
  public static final String HH_MM_SS = "HH:mm:ss";
  public static final String HH_MM_SS_SSS = "HH:mm:ss.SSS";

  /** All supported patterns for parsing. */
  public static final String[] ALL_PATTERNS = {
    YYYY_MM_DD,
    YYYY_MM_DD_HH_MM,
    YYYY_MM_DD_HH_MM_SS,
    YYYY_MM_DD_HH,
    YYYY_MM_DD_T_HH,
    YYYY_MM_DD_T_HH_MM,
    YYYY_MM_DD_T_HH_MM_SS,
    YYYY_MM_DD_T_HH_MM_SS_SSS,
    YYYY_MM_DD_T_HH_MM_SS_SSSS,
    YYYY_MM_DD_T_HH_MM_SS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_SSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_Z,
    YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
    HH_MM,
    HH_MM_SS,
    HH_MM_SS_SSS
  };

  /** Common year-month-date patterns (no pure time-only). */
  public static final String[] STANDARD_YEAR_MONTH_DATE = {
    YYYY_MM_DD,
    YYYY_MM_DD_HH_MM,
    YYYY_MM_DD_HH_MM_SS,
    YYYY_MM_DD_HH,
    YYYY_MM_DD_T_HH,
    YYYY_MM_DD_T_HH_MM,
    YYYY_MM_DD_T_HH_MM_SS,
    YYYY_MM_DD_T_HH_MM_SS_SSS,
    YYYY_MM_DD_T_HH_MM_SS_SSSS,
    YYYY_MM_DD_T_HH_MM_SS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_SSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_Z,
    YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
  };

  /**
   * Build a date-time pattern based on the input string format. Automatically detects separators,
   * timezone info, and fractional seconds.
   */
  public static String buildDateTimePattern(String input) {
    if (input == null || input.trim().isEmpty()) {
      return YYYY_MM_DD_T_HH_MM_SS_SSS_XXX; // default
    }

    String s = input.trim();

    // Detect date separator
    char dateSep = s.contains("/") ? '/' : '-';

    // Start with date
    StringBuilder p = new StringBuilder("yyyy");
    if (s.length() >= 7) p.append(dateSep).append("MM");
    if (s.length() >= 10) p.append(dateSep).append("dd");

    // Check for time part
    int tIndex = s.indexOf('T');
    int spaceIndex = s.indexOf(' ');
    int timeStart = Math.max(tIndex, spaceIndex);

    if (timeStart >= 0) {
      char timeSep = (tIndex >= 0) ? 'T' : ' ';
      p.append('\'').append(timeSep).append('\'');

      String time = s.substring(timeStart + 1);

      // Count colon-separated time fields
      int colons = (int) time.chars().filter(ch -> ch == ':').count();
      if (colons >= 0) p.append("HH");
      if (colons >= 1) p.append(":mm");
      if (colons >= 2) p.append(":ss");

      // Handle fractional seconds
      if (time.contains(".")) {
        String fracPart = time.substring(time.indexOf('.') + 1);
        // Remove timezone part to count only fraction digits
        fracPart = fracPart.replaceAll("[Zz]|[+-]\\d{2}:?\\d{2}.*$", "");
        int fracLen = fracPart.length();
        if (fracLen > 0) {
          p.append(".").append("S".repeat(Math.min(fracLen, 9)));
        }
      }

      // Handle timezone
      if (time.matches(".*[Zz]$")) {
        p.append("'Z'");
      } else if (time.matches(".*[+-]\\d{2}:?\\d{2}$")) {
        p.append("XXX");
      }
    }

    return p.toString();
  }

  public static String normalizeDate(String input) {
    if (input == null || input.trim().isEmpty()) {
      return "";
    }

    // Keep only digits and date separators in the first 10 chars (date part)
    String s = input.trim();

    // If there’s a time portion (e.g. “2025:02:12T10:15”),
    // separate date and time to avoid replacing colons in time
    String datePart;
    String timePart = "";
    int tIndex = s.indexOf('T');
    int spaceIndex = s.indexOf(' ');
    int cut = (tIndex >= 0) ? tIndex : (spaceIndex >= 0 ? spaceIndex : s.length());

    datePart = s.substring(0, cut);
    if (cut < s.length()) {
      timePart = s.substring(cut); // preserve time if present
    }

    // Replace any non-digit separator in the date part with '/'
    datePart = datePart.replaceAll("[-:.]", "/");

    return datePart + timePart;
  }
}
