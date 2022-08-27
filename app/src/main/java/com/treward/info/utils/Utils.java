package com.treward.info.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class  Utils {

    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void removeAllShared(Context con) {

        SharedPreferences sp = con
                .getApplicationContext()
                .getSharedPreferences(Const.PREFRENCE, Context.MODE_PRIVATE);
        sp.edit().clear().commit();

    }

    public static void saveInShared(Context con, String key, String value) {

        SharedPreferences sp = con
                .getApplicationContext()
                .getSharedPreferences(Const.PREFRENCE, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();

        sp.edit().putString(key, value).commit();

    }
    public static void savegame(Context con, String key, String value) {

        SharedPreferences sp = con
                .getApplicationContext()
                .getSharedPreferences(Const.PREFRENCE, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();

        sp.edit().putString(key, value).commit();

    }
    public static String getFrmSharedgame(Context c, String key, String defaultValue) {

        SharedPreferences sp = c.getApplicationContext().getSharedPreferences(
                Const.PREFRENCE, Context.MODE_PRIVATE);

        return sp.getString(key, defaultValue);

    }

    public static String getFrmShared(Context c, String key, String defaultValue) {

        SharedPreferences sp = c.getApplicationContext().getSharedPreferences(
                Const.PREFRENCE, Context.MODE_PRIVATE);

        return sp.getString(key, defaultValue);

    }

    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object fetch(String address) throws MalformedURLException, IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public static String getShortMonthName(int num){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        return monthNames[num];

    }

    public static int[] getThreeLowest(Integer[] array) {
        int[] lowestValues = new int[4];
        Arrays.fill(lowestValues, Integer.MAX_VALUE);

        for(int n : array) {
            if(n < lowestValues[3]) {
                lowestValues[3] = n;
                Arrays.sort(lowestValues);
            }
        }
        return lowestValues;
    }

    public static Long getSystemUTCTime(){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        Long l = System.currentTimeMillis();

        return l;
    }

    public static Date GetUTCdatetimeAsDate()
    {
        //note: doesn't check for null
        return StringDateToDate(GetUTCdatetimeAsString());
    }

    public static String GetUTCdatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }

    public static Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);


        int gmtOffset = TimeZone.getDefault().getRawOffset();
        //long current = milliSeconds + gmtOffset;

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(milliSeconds);
        Log.d("Local Time is : ", formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    public static String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

}
