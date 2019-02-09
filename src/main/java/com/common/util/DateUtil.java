//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_HHMM_FORMAT = "/MM/dd/HH/mm";
    public static final String DATE_YYYY_MM_DD_HH_FORMAT = "yyyy-MM-dd-HH";
    public static final String DATE_YYYY_MM_DD_HH_MM_FORMAT = "yyyy-MM-dd-HH-mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String MONTH_FORMAT = "yyyy-MM";
    public static final String DATE_CH_FORMAT = "yyyy年MM月dd日";
    public static final String HHMM_FORMAT = "HH:mm";
    public static final String HHMMSS_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_SSS_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String[] parsePatterns = new String[]{"yyyy/MM/dd HH:mm", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "yyyy-MM-dd", "dd/MM/yyyy"};

    public DateUtil() {
    }

    public static Date parse(String dateTime, String format) {
        try {
            return DateUtils.parseDate(dateTime, new String[]{format});
        } catch (ParseException var3) {
            logger.error("convert String to date error", var3);
            return null;
        }
    }

    public static Date parseDate(String dateTime) {
        try {
            return DateUtils.parseDate(dateTime, parsePatterns);
        } catch (ParseException var2) {
            logger.error("convert String to date error", var2);
            return null;
        }
    }

    public static Date parseDate(String dateTime, String pattern) {
        try {
            return parse(dateTime, pattern);
        } catch (Exception var3) {
            logger.error("convert String to date error", var3);
            return null;
        }
    }

    public static String StringToString(String date, String parttern) {
        return StringToString(date, (String)null, parttern);
    }

    public static String StringToString(String date, String olddParttern, String newParttern) {
        String dateString = null;
        if(olddParttern == null) {
            DateStyleEnum myDate = getDateStyle(date);
            if(myDate != null) {
                Date myDate1 = StringToDate(date, myDate.getValue());
                dateString = DateToString(myDate1, newParttern);
            }
        } else {
            Date myDate2 = StringToDate(date, olddParttern);
            dateString = DateToString(myDate2, newParttern);
        }

        return dateString;
    }

    public static String DateToString(Date date, String parttern) {
        String dateString = null;
        if(date != null) {
            try {
                dateString = getDateFormat(parttern).format(date);
            } catch (Exception var4) {
                ;
            }
        }

        return dateString;
    }

    private static DateStyleEnum getDateStyle(String date) {
        DateStyleEnum dateStyle = null;
        HashMap map = new HashMap();
        ArrayList timestamps = new ArrayList();
        DateStyleEnum[] var4 = DateStyleEnum.values();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            DateStyleEnum style = var4[var6];
            Date dateTmp = StringToDate(date, style.getValue());
            if(dateTmp != null) {
                timestamps.add(Long.valueOf(dateTmp.getTime()));
                map.put(Long.valueOf(dateTmp.getTime()), style);
            }
        }

        dateStyle = (DateStyleEnum)map.get(Long.valueOf(getAccurateDate(timestamps).getTime()));
        return dateStyle;
    }

    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0L;
        HashMap map = new HashMap();
        ArrayList absoluteValues = new ArrayList();
        if(timestamps != null && timestamps.size() > 0) {
            if(timestamps.size() > 1) {
                for(int minAbsoluteValue = 0; minAbsoluteValue < timestamps.size(); ++minAbsoluteValue) {
                    for(int j = minAbsoluteValue + 1; j < timestamps.size(); ++j) {
                        long timestampsLastTmp = Math.abs(((Long)timestamps.get(minAbsoluteValue)).longValue() - ((Long)timestamps.get(j)).longValue());
                        absoluteValues.add(Long.valueOf(timestampsLastTmp));
                        long[] timestampTmp = new long[]{((Long)timestamps.get(minAbsoluteValue)).longValue(), ((Long)timestamps.get(j)).longValue()};
                        map.put(Long.valueOf(timestampsLastTmp), timestampTmp);
                    }
                }

                long var15 = -1L;
                if(!absoluteValues.isEmpty()) {
                    var15 = ((Long)absoluteValues.get(0)).longValue();
                }

                for(int var16 = 0; var16 < absoluteValues.size(); ++var16) {
                    for(int dateOne = var16 + 1; dateOne < absoluteValues.size(); ++dateOne) {
                        if(((Long)absoluteValues.get(var16)).longValue() > ((Long)absoluteValues.get(dateOne)).longValue()) {
                            var15 = ((Long)absoluteValues.get(dateOne)).longValue();
                        } else {
                            var15 = ((Long)absoluteValues.get(var16)).longValue();
                        }
                    }
                }

                if(var15 != -1L) {
                    long[] var17 = (long[])map.get(Long.valueOf(var15));
                    if(absoluteValues.size() > 1) {
                        timestamp = Math.max(var17[0], var17[1]);
                    } else if(absoluteValues.size() == 1) {
                        long var18 = var17[0];
                        long dateTwo = var17[1];
                        if(Math.abs(var18 - dateTwo) < 100000000000L) {
                            timestamp = Math.max(var17[0], var17[1]);
                        } else {
                            long now = (new Date()).getTime();
                            if(Math.abs(var18 - now) <= Math.abs(dateTwo - now)) {
                                timestamp = var18;
                            } else {
                                timestamp = dateTwo;
                            }
                        }
                    }
                }
            } else {
                timestamp = ((Long)timestamps.get(0)).longValue();
            }
        }

        if(timestamp != 0L) {
            date = new Date(timestamp);
        }

        return date;
    }

    public static Date StringToDate(String date) {
        Object dateStyle = null;
        return StringToDate(date, (DateStyleEnum)dateStyle);
    }

    public static Date StringToDate(String date, DateStyleEnum dateStyle) {
        Date myDate = null;
        if(dateStyle == null) {
            ArrayList timestamps = new ArrayList();
            DateStyleEnum[] var4 = DateStyleEnum.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                DateStyleEnum style = var4[var6];
                Date dateTmp = StringToDate(date, style.getValue());
                if(dateTmp != null) {
                    timestamps.add(Long.valueOf(dateTmp.getTime()));
                }
            }

            myDate = getAccurateDate(timestamps);
        } else {
            myDate = StringToDate(date, dateStyle.getValue());
        }

        return myDate;
    }

    public static Date StringToDate(String date, String parttern) {
        Date myDate = null;
        if(date != null) {
            try {
                myDate = getDateFormat(parttern).parse(date);
            } catch (Exception var4) {
                ;
            }
        }

        return myDate;
    }

    private static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }

    public static Date parseDateTime(String dateTime) throws ParseException {
        return parse(dateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getStartDate(Date date) {
        String dateStr = formatYYYYMMDD(date);

        try {
            return parse(dateStr, "yyyy-MM-dd");
        } catch (Exception var3) {
            throw new ApplicationException("获取日期开始时间出错");
        }
    }

    public static Date getEndDate(Date date) {
        String dateStr = formatYYYYMMDD(Calendar.getInstance().getTime());
        dateStr = dateStr + " 23:59:59";

        try {
            return parse(dateStr, "yyyy-MM-dd HH:mm:ss");
        } catch (Exception var3) {
            throw new ApplicationException("获取日期结束时间出错");
        }
    }

    public static String formatDateTime(Date date, String format) {
        if(date == null) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        }
    }

    public static Date parseChDate(String dateTime) throws ParseException {
        return parse(dateTime, "yyyy年MM月dd日");
    }

    public static String formatHHmm(Date date) {
        return formatDateTime(date, "HH:mm");
    }

    public static String formatHHmmss(Date date) {
        return formatDateTime(date, "HH:mm:ss");
    }

    public static String formatYYYYMMDD(Date date) {
        return formatDateTime(date, "yyyy-MM-dd");
    }

    public static String formatYYYYMM(Date date) {
        return formatDateTime(date, "yyyy-MM");
    }

    public static String formatChYYYYMMDD(Date date) {
        return formatDateTime(date, "yyyy年MM月dd日");
    }

    public static String format(Date date) {
        return formatDateTime(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDATEHHMMFORMAT(Date date) {
        return formatDateTime(date, "/MM/dd/HH/mm");
    }

    public static String formatDateHHMM(Date date) {
        return formatDateTime(date, "yyyy-MM-dd-HH-mm");
    }

    public static String formatDateHH(Date date) {
        return formatDateTime(date, "yyyy-MM-dd-HH");
    }

    public static String getCurrDate() {
        return format(new Date());
    }

    public static Date translateDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    public static Date toZeroDateTime(Date date) {
        String dateStr = formatDateTime(date, "yyyy-MM-dd") + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException var4) {
            logger.info("日期处理出错：" + var4.getMessage(), var4);
        }

        return date;
    }

    public static Date toYyyymmddDateTime(Date date) {
        String dateStr = formatDateTime(date, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException var4) {
            logger.info("日期处理出错：" + var4.getMessage(), var4);
        }

        return date;
    }

    public static Date getPrevDay(Date date) {
        if(date == null) {
            return null;
        } else {
            int year = getYear(date);
            int month = getMonth(date);
            int day = getMonthDay(date);
            if(year >= 1900 && month >= 0 && month <= 11 && day >= 1 && day <= 31) {
                date = getPrevDay(year, month, day);

                try {
                    date = parseDateTime(toZeroDate(date));
                } catch (ParseException var5) {
                    logger.error("DateUtil.getNextDay parseDateTime error:", var5);
                }

                return date;
            } else {
                return null;
            }
        }
    }

    public static Date getPrevDay(int year, int month, int day) {
        if(!checkDay(year, month, day)) {
            return null;
        } else if(year == 1900 && month == 0 && day == 1) {
            return toDate(1900, 0, 1);
        } else {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, day);
            cal.add(6, -1);
            return cal.getTime();
        }
    }

    public static Date getNextDay(Date date) {
        if(date == null) {
            return null;
        } else {
            int year = getYear(date);
            int month = getMonth(date);
            int day = getMonthDay(date);
            date = getNextDay(year, month, day);

            try {
                date = parseDateTime(toZeroDate(date));
            } catch (ParseException var5) {
                logger.error("DateUtil.getNextDay parseDateTime error:", var5);
            }

            return date;
        }
    }

    public static String toZeroDate(Date date) {
        return formatDateTime(date, "yyyy-MM-dd") + " 00:00:00";
    }

    public static Date getNextDay(int year, int month, int day) {
        if(!checkDay(year, month, day)) {
            return null;
        } else {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, day);
            cal.add(6, 1);
            return cal.getTime();
        }
    }

    public static Date toLastDateTime(Date date) {
        if(date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, 23);
            calendar.set(12, 59);
            calendar.set(13, 59);
            return calendar.getTime();
        }
    }

    public static int compareDay(Date d0, Date d1) {
        return d0.compareTo(d1);
    }

    public static boolean isInDayZone(Date d0, Date d1, Date d2) {
        return compareDay(d0, d1) >= 0 && compareDay(d1, d2) <= 0;
    }

    public static Date getThisDayByDay(Date date, int days) {
        if(days == 0) {
            return date;
        } else if(date == null) {
            return null;
        } else {
            int year = getYear(date);
            int month = getMonth(date);
            int day = getMonthDay(date);
            if(!checkDay(year, month, day)) {
                return null;
            } else {
                Calendar cal = GregorianCalendar.getInstance();
                cal.set(year, month, day);
                cal.add(6, days);
                Date findDate = cal.getTime();
                Date date_1900_1_1 = toDate(1900, 0, 1);
                return compareDay(findDate, date_1900_1_1) < 0?date_1900_1_1:findDate;
            }
        }
    }

    public static int getMaxDays(int year, int month) {
        if(year >= 1900 && month >= 0 && month <= 11) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, 1);
            return cal.getActualMaximum(5);
        } else {
            return 0;
        }
    }

    public static boolean checkDay(int year, int month, int monthDay) {
        if(year >= 1900 && month >= 0 && month <= 11 && monthDay >= 1 && monthDay <= 31) {
            int maxDay = getMaxDays(year, month);
            return monthDay <= maxDay;
        } else {
            return false;
        }
    }

    public static Date toDate(int year, int month, int day) {
        if(!checkDay(year, month, day)) {
            return null;
        } else {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, day);
            return cal.getTime();
        }
    }

    public static int getYear(Date date) {
        return date == null?-1:date.getYear() + 1900;
    }

    public static int getMonth(Date date) {
        return date == null?-1:date.getMonth();
    }

    public static String getMonthAndDay(Date startDate, Date endDate) {
        return startDate != null && endDate != null?startDate.getMonth() + 1 + "月" + startDate.getDate() + "日--" + (endDate.getMonth() + 1) + "月" + endDate.getDate() + "日":"";
    }

    public static int getMonthDay(Date date) {
        return date == null?-1:date.getDate();
    }

    public static int getWeekDay(Date date) {
        if(date == null) {
            return -1;
        } else {
            int d = date.getDay();
            if(d == 0) {
                d = 7;
            }

            return d;
        }
    }

    public static int getHours(Date date) {
        return date == null?-1:date.getHours();
    }

    public static int getMinutes(Date date) {
        return date == null?-1:date.getMinutes();
    }

    public static Date getFirstDayOfWeek(Date date) {
        if(date == null) {
            return null;
        } else {
            int year = getYear(date);
            int month = getMonth(date);
            int day = getMonthDay(date);
            return getFirstDayOfWeek(year, month, day);
        }
    }

    public static Date getFirstDayOfWeek(int year, int month, int day) {
        if(!checkDay(year, month, day)) {
            return null;
        } else {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, day);
            cal.add(7, 1 - getWeekDay(toDate(year, month, day)));
            return cal.getTime();
        }
    }

    public static Date getEndDayOfWeek(Date date) {
        if(date == null) {
            return null;
        } else {
            int year = getYear(date);
            int month = getMonth(date);
            int day = getMonthDay(date);
            return getEndDayOfWeek(year, month, day);
        }
    }

    public static Date getEndDayOfWeek(int year, int month, int day) {
        if(!checkDay(year, month, day)) {
            return null;
        } else {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, day);
            cal.add(7, 7 - getWeekDay(toDate(year, month, day)));
            return cal.getTime();
        }
    }

    public static Date getFirstDayOfPrevWeek(Date date) {
        if(date == null) {
            return null;
        } else {
            int year = getYear(date);
            int month = getMonth(date);
            int day = getMonthDay(date);
            return getFirstDayOfPrevWeek(year, month, day);
        }
    }

    public static Date getFirstDayOfPrevWeek(int year, int month, int day) {
        if(!checkDay(year, month, day)) {
            return null;
        } else if(year == 1900 && month == 0 && day <= 7) {
            return toDate(year, month, 1);
        } else {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(year, month, day);
            cal.add(6, -7);
            Date newDate = cal.getTime();
            return getFirstDayOfWeek(getYear(newDate), getMonth(newDate), getMonthDay(newDate));
        }
    }

    public static Date nextSecond(Date startDate) {
        Calendar cal = Calendar.getInstance();
        if(startDate != null) {
            cal.setTime(startDate);
        }

        cal.add(13, 1);
        return cal.getTime();
    }

    public static Date nextMinutes(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        if(date != null) {
            cal.setTime(date);
        }

        cal.add(12, minute);
        return cal.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        return toDate(year, month, 1);
    }

    public static void main(String[] args) {
        Date toDate = new Date();
        System.out.println(format(nextMinutes(toDate, 20)));
    }
}
