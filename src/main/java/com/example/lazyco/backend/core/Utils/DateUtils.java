package com.example.lazyco.backend.core.Utils;

public class DateUtils {
//    private static final ThreadLocal<DateFormat[]> dateFormats =
//            ThreadLocal.withInitial(
//                    () ->
//                            new DateFormat[] {
//                                    new SimpleDateFormat(DateTimeFormatEnum.yyyy_MM_dd_T_HH_mm_ssXXX.getValue()),
//                                    new SimpleDateFormat(DateTimeFormatEnum.yyyy_MM_dd_HH_mm_ss.getValue()),
//                                    new SimpleDateFormat(DateTimeFormatEnum.yyyy_MM_dd.getValue())
//                            });
//
//    private static final ThreadLocal<DateFormat[]> timeFormats =
//            ThreadLocal.withInitial(
//                    () -> new DateFormat[] {new SimpleDateFormat(DateTimeFormatEnum.HH_mm_ss.getValue())});
//
//    public static Date deserializeDate(String dateString) {
//        DateFormat[] formats = dateFormats.get();
//        for (DateFormat dateFormat : formats) {
//            try {
//                return dateFormat.parse(dateString);
//            } catch (Exception e) {
//                // Ignoring the exception and trying the next date format
//            }
//        }
//        throw new JsonParseException("Unparseable date: " + dateString);
//    }
//
//    public static Time deserializeTime(String timeString) {
//        DateFormat[] formats = timeFormats.get();
//        for (DateFormat timeFormat : formats) {
//            try {
//                Date date = timeFormat.parse(timeString);
//                return new Time(date.getTime());
//            } catch (Exception e) {
//                // Ignoring the exception and trying the next date format
//            }
//        }
//        throw new JsonParseException("Unparseable date: " + timeString);
//    }
}
