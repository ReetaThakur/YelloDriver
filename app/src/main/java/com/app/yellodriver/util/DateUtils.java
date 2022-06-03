package com.app.yellodriver.util;


import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Performs the Date format operation.
 */
public class DateUtils {
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSSS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_OSP = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";

    public static String convertDate(final String strSourceFormat, final String strDestinationFormat, final String sourceDate) {
        final SimpleDateFormat sourceFormat = new SimpleDateFormat(strSourceFormat, Locale.getDefault());
        final SimpleDateFormat DesiredFormat = new SimpleDateFormat(strDestinationFormat, Locale.getDefault());

        final Date date;
        try {
            date = sourceFormat.parse(sourceDate);

            return DesiredFormat.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //  10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //  20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //  30    31
                    "th", "st"};

    public static String convertDateToSuffixFormat(String strInputDate) {
        //String strInputDate ="2020-08-07T06:22:55.137688";
        String convertedFormat = strInputDate;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        SimpleDateFormat output = new SimpleDateFormat("MMM, yyyy");

        SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
        try {
            Date originalDate = input.parse(strInputDate);                 // parse input
            int day = Integer.parseInt(formatDayOfMonth.format(originalDate));
            String dayStr = day + suffixes[day];
            convertedFormat = dayStr + " " + output.format(originalDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        YLog.e("Parseconverted", convertedFormat);
        return convertedFormat;
    }

    public static String getDateFromUTCTimestamp(long mTimestamp, String mDateFormate) {
        String date = null;
        try {
            //Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(mTimestamp * 1000L);
            date = DateFormat.format(mDateFormate, cal.getTimeInMillis()).toString();

            /*SimpleDateFormat formatter = new SimpleDateFormat(mDateFormate);
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            date = dateFormatter.format(value);*/
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDurationFromTwoDates(String startInputDate, String endInputDate) {
        //"start_at": "2020-09-25T14:15:16.452",
        //"end_at": "2020-09-25T14:16:05.252",
        String convertedFormat = "";
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        try {
            Date originalStartDate = input.parse(startInputDate);
            Date originalEndDate = input.parse(endInputDate);

            long diff = originalEndDate.getTime() - originalStartDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;


            if (hours > 0) {
                convertedFormat = hours + " Hrs ";
            }
            if (minutes > 0) {
                convertedFormat += minutes + " Minutes";
            }
            if (convertedFormat.trim().length() == 0) {
                return "NA";
            }
            return convertedFormat;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        YLog.e("Parseconverted", convertedFormat);
        return "NA";
    }

    public static String getRemainingDurationFromRoute(int secondstoDestination) {
        int sec = secondstoDestination % 60;
        int hr = secondstoDestination / 60;
        int min = hr % 60;
        hr = hr / 60;

        String arrivalTime = "Arrival in ";
        if (hr != 0) {
            arrivalTime += hr + "hr(s)";
        }
        if (min != 0) {
            arrivalTime += min + "min(s)";
        }
                /*if (sec != 0) {
                    arrivalTime += sec + "second(s)";
                }*/
        return arrivalTime;
    }

    public static String convertDateToNewFormat(String strInputDate, String newFormat) {
        //String strInputDate ="2020-08-07T06:22:55.137688";
        String convertedFormat = strInputDate;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        SimpleDateFormat output = new SimpleDateFormat(newFormat);


        try {
            Date originalDate = input.parse(strInputDate);                 // parse input
            convertedFormat = output.format(originalDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        YLog.e("Parseconverted", convertedFormat);
        return convertedFormat;
    }

    public static String timeAgo(String dataDate) {
        String convTime = null;

        String prefix = "Since ";
        String suffix = "";

        YLog.e("DateUtils", dataDate);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSSS);
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = prefix + second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = prefix + minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime = prefix + hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = prefix + (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = prefix + (day / 30) + " Months " + suffix;
                } else {
                    convTime = prefix + (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = prefix + day + " Days " + suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            YLog.e("ConvTimeE", e.getMessage());
        }

        return convTime;

    }

    public static String age(String dataDate) {
        String convTime = null;

        String prefix = "";
        String suffix = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_OSP);
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = prefix + second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = prefix + minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime = prefix + hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = prefix + (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = prefix + (day / 30) + " Months " + suffix;
                } else {
                    convTime = prefix + (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = prefix + day + " Days " + suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            YLog.e("ConvTimeE", e.getMessage());
        }

        return convTime;

    }
}
