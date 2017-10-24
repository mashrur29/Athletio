package receivers;

/**
 * Created by tanvir on 9/26/17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.blogspot.athletio.R;
import com.blogspot.athletio.ShowEventActivity;

import storage.SharedPrefData;
/// Set event notification and alarm
public class EventReminderReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String Noti_title = intent.getExtras().getString("title");
        String Noti_message = intent.getExtras().getString("notes");
        int reqid=intent.getIntExtra("id",1);
        Intent notificationIntent = new Intent(context,ShowEventActivity.class);
        String key=intent.getExtras().getString("event");
        notificationIntent.putExtra("EVENT",key);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ShowEventActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        SharedPrefData sharedPrefData=new SharedPrefData(context);
        sharedPrefData.removeEventKey(key);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = builder.setContentTitle(Noti_title )
                .setContentText(Noti_message)
                .setTicker("New Event Alert!")
                .setSmallIcon(R.drawable.logo)
                .setVibrate(new long[] { 1000, 2000,1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reqid, notification);

    }
}