package ecke.smstorest.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import ecke.smstorest.controller.SMSReceiver;

public class ContinuityService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(new SMSReceiver(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        registerReceiver(new SMSReceiver(), new IntentFilter("android.provider.Telephony.SMS_DELIVER"));
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}