package com.example.amiao.specialalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String string = intent.getExtras().getString("extra");

        Log.e("We are in the receiver.", "Yay!");

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, Ringtone.class);

        service_intent.putExtra("extra", string);

        // start the ringtone
        context.startService(service_intent);
    }
}

