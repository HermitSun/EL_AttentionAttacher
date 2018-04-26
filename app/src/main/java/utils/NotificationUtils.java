package utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.frog.el_attentionattacher.R;

/**
 * Created by LaoZhao on 2017/11/19.(Thanks!)
 * Refactored and updated by WenSun on 2018/4/6.
 * version 1.3
 * 用于实现可兼容的构建、发送通知（已在6.0、7.0、8.0测试）
 * 提示：创建实例时需传入Context
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    private Notification.Builder builder = null;
    private NotificationCompat.Builder builder_25 = null;

    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    //构造时需要传入Context
    public NotificationUtils(Context context) {
        super(context);
    }

    //默认高优先级
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void getChannelNotification(String title, String content) {
        builder = new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
    }

    public void getNotification_25(String title, String content) {
        builder_25 = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
    }

    //目前默认AutoCancel,实现了可选支持PendingIntent
    //增加了构建通知的功能，减少使用其他功能时的不便
    public Notification buildNotification(String title, String content, PendingIntent... pi) {
        Notification notification;
        if (pi.length > 0) {
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel();
                getChannelNotification(title, content);
                notification = builder.setContentIntent(pi[0]).build();
            } else {
                getNotification_25(title, content);
                notification = builder_25.setContentIntent(pi[0]).build();
            }
        } else {
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel();
                getChannelNotification(title, content);
                notification = builder.build();
            } else {
                getNotification_25(title, content);
                notification = builder_25.build();
            }
        }
        return notification;
    }

    //该方法适用于只发送通知，使用默认图标
    public void sendNotification(String title, String content, PendingIntent... pi) {
        Notification notification;
        if (pi.length > 0) {
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel();
                getChannelNotification(title, content);
                notification = builder.setContentIntent(pi[0]).build();
                getManager().notify(1, notification);
            } else {
                getNotification_25(title, content);
                notification = builder_25.setContentIntent(pi[0]).build();
                getManager().notify(1, notification);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel();
                getChannelNotification(title, content);
                notification = builder.build();
                getManager().notify(1, notification);
            } else {
                getNotification_25(title, content);
                notification = builder_25.build();
                getManager().notify(1, notification);
            }
        }
    }
    //构建进度条通知，适用于下载等情况；进度条采用默认图标和整数进度
    public Notification buildProgressNotification(Activity target, String title, int progress) {
        Intent intent = new Intent(this, target.getClass());
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            builder = new Notification.Builder(getApplicationContext(), id);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setContentTitle(title);
            if (progress >= 0) {
                builder.setContentText(progress + "%");
                builder.setProgress(100, progress, false);
            }
            return builder.build();
        } else {
            builder_25 = new NotificationCompat.Builder(this);
            builder_25.setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setContentTitle(title);
            if (progress >= 0) {
                builder_25.setContentText(progress + "%");
                builder_25.setProgress(100, progress, false);
            }
            return builder_25.build();
        }
    }
}
