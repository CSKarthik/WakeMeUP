package com.ex.wakemeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.AlarmClock;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.support.v4.app.ActivityCompat.startActivity;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by kcs on 6/18/14.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    //MainActivity m = new MainActivity();

    @Override
    public void onReceive(Context context, Intent intent) {
       /* PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("One time Timer : ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));

        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        //Release the lock
        wl.release();*/
        Bundle extras = intent.getExtras();
        double lat = extras.getDouble("lat");
        double lon = extras.getDouble("lon");
        double latval = extras.getDouble("latval");
        double lonval = extras.getDouble("lonval");

        double d = getDistanceInMeters(lat,lon,latval,lonval);
        //if(d<500)
            Toast.makeText(context,"Distance is "+d,Toast.LENGTH_SHORT).show();

    }

    public void SetAlarm(Context context, double lat, double lon, double latval, double lonval)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        intent.putExtra("laval",latval);
        intent.putExtra("lonval",lonval);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 60 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }

    public Double getDistanceInMeters(double latitude, double longitude, double latval , double lonval) {
        Double dlon = Math.toRadians(lonval) - Math.toRadians(longitude);
        Double dlat = Math.toRadians(latval) - Math.toRadians(latitude);

        Double a = (sin(dlat/2))*(sin(dlat/2)) + cos(latval) * cos(latitude) * (sin(dlon/2))*(sin(dlon/2));
        Double c = 2 * atan2( sqrt(a), sqrt(1-a) );
        Double dist = 6373 * c * 1000;       // Radius of earth is 6373 kms
        return dist;
    }

    /*public void ringAlarmSound() {
        //Toast.makeText(this,"You have reached your destination",Toast.LENGTH_SHORT).show();

        //Setting alarm
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute+1);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
    }*/



}
