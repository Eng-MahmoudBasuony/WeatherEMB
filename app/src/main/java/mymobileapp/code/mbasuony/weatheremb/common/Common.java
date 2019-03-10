package mymobileapp.code.mbasuony.weatheremb.common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

final public class Common
{

    public static final String APP_ID="84ac124788bc2a57fc3335b3eac8e9d5";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt)
    {
        Date date =new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm EEE MM YYYY");
        String format=simpleDateFormat.format(date);
        return format;
    }
    public static String convertUnixToHour(long dt)
    {
        Date date =new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
        String format=simpleDateFormat.format(date);
        return format;
    }
}
