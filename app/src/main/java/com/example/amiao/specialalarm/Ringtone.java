package com.example.amiao.specialalarm;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class Ringtone extends Service {

    MediaPlayer song;
    Math math;
    int startId;
    static boolean isMusicOn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getExtras().getString("extra");

        assert state != null;
        if(state.equals("on")){
            startId = 1;
        } else if(state.equals("off")){
            startId = 0;
        } else if(state.equals("submit")){
            startId = 2;
        } else {
            startId = 0;
        }

        if (!this.isMusicOn && startId ==1){
            song = MediaPlayer.create(this, R.raw.pentakill);
            song.setLooping(true);
            song.start();
            Log.e("you are here: ","after song.start()");

            this.isMusicOn = true;
            this.startId = 0;

        } else if(this.isMusicOn && startId == 0 && MainActivity.count == 1){

            math = new Math();
            MainActivity.setQuestionTV(math.getNum1() + " + " + math.getNum2() + " = ?");
            math.computeSum();
            this.isMusicOn = true;
            this.startId = 0;

        } else if (this.isMusicOn && startId == 2 && MainActivity.isCorrect){

            song.stop();
            song.reset();

            this.isMusicOn = false;
            this.startId = 0;
            MainActivity.reset();

        } else if (!this.isMusicOn && startId == 0){

            this.isMusicOn = false;
            this.startId = 0;
        } else if (this.isMusicOn && startId == 1){

            this.isMusicOn = true;
            this.startId = 1;

        } else {

        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.isMusicOn = false;
    }
}