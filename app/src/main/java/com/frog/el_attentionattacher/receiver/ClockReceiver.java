package com.frog.el_attentionattacher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.Toast;

import com.frog.el_attentionattacher.AlarmActivity;

import utils.ToastUtil;

public class ClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        context.startActivity(alarmIntent);
    }
}
