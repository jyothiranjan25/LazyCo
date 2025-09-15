package com.example.lazyco.backend.core.DateUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

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
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_SSSS =
      "yyyy-MM-dd'T'HH:mm:ss.SSSS"; // 4-digit fraction
  public static final String YYYY_MM_DD_T_HH_MM_SS_XXX = "yyyy-MM-dd'T'HH:mm:ssXXX";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_SSSS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX";

  // Optional time-zone Zulu (UTC) support
  public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  // Slash-separated legacy formats
  public static final String YYYY_SLASH_MM_SLASH_DD = "yyyy/MM/dd";
  public static final String YYYY_SLASH_MM_SLASH_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

  // Time only
  public static final String HH_MM_SS = "HH:mm:ss";

  /** All supported patterns beginning with YYYY-MM-DD (or YYYY/MM/DD). */
  public static final String[] ALL_PATTERNS = {
    YYYY_MM_DD,
    YYYY_MM_DD_HH_MM,
    YYYY_MM_DD_HH_MM_SS,
    YYYY_MM_DD_HH,
    YYYY_MM_DD_T_HH,
    YYYY_MM_DD_T_HH_MM,
    YYYY_MM_DD_T_HH_MM_SS,
    YYYY_MM_DD_T_HH_MM_SS_SSS,
    YYYY_MM_DD_T_HH_MM_SS_SSS_SSSS,
    YYYY_MM_DD_T_HH_MM_SS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_SSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_SSS_SSSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_Z,
    YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
    YYYY_SLASH_MM_SLASH_DD,
    YYYY_SLASH_MM_SLASH_DD_HH_MM_SS,
    HH_MM_SS
  };

  /** Common year-month-date patterns (no pure time-only). */
  public static final String[] STANDARD_YEAR_MONTH_DATE = {
    YYYY_MM_DD,
    YYYY_MM_DD_HH_MM,
    YYYY_MM_DD_HH_MM_SS,
    YYYY_MM_DD_T_HH_MM,
    YYYY_MM_DD_T_HH_MM_SS,
    YYYY_MM_DD_T_HH_MM_SS_SSS,
    YYYY_MM_DD_T_HH_MM_SS_SSS_XXX,
    YYYY_MM_DD_T_HH_MM_SS_Z,
    YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
    YYYY_SLASH_MM_SLASH_DD,
    YYYY_SLASH_MM_SLASH_DD_HH_MM_SS
  };
}
