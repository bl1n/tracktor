package com.elegion.tracktor.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.elegion.tracktor.event.StopTrackFromBREvent;

import org.greenrobot.eventbus.EventBus;

public class StopTrackReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new StopTrackFromBREvent());
        Log.d("Debug", "onReceive: ");
    }
}
