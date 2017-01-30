package com.example.amiao.specialalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // to make alarm manager
    AlarmManager alarmManager;
    Ringtone ringtone;
    TimePicker timePicker;
    DatePicker datePicker;
    static TextView update_msg;
    static TextView question;
    static EditText answer;
    EditText days_on_num;
    EditText days_off_num;
    Context context;
    PendingIntent pending_intent;
    static boolean isCorrect = false;
    static int count = 0;
    static String givenAnswer;
    static int daysInterval;
    final int ONEDAY = 1000 * 60 * 60 * 24;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        ringtone = new Ringtone();

        // Initialize alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Initialize time picker
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        // Initialize date picker
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        // Initialize TextView and EditView
        update_msg = (TextView) findViewById(R.id.update_msg);
        question = (TextView) findViewById(R.id.question);
        answer = (EditText) findViewById(R.id.answer);
        days_on_num = (EditText) findViewById(R.id.days_on_num);

        answer.addTextChangedListener(answerTextWatcher);
        days_on_num.addTextChangedListener(onDaysTextWatcher);

        // Create an instance of calendar
        final Calendar calendar = Calendar.getInstance();


        // Create an intent to the Receiver class
        final Intent intent = new Intent(this.context, Receiver.class);



        // Initialize alarm on button
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        // Create an onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                // comment out to skip the waiting time.
                /*
                Date now = new Date(System.currentTimeMillis());
                if (calendar.getTime().before(now)) {
                    calendar.add(Calendar.MONTH, 1);
                    day ++;
                }
*/
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);
                String year_string = String.valueOf(year);
                String month_string = String.valueOf(month + 1);
                String day_string = String.valueOf(day);

                if (minute < 10 ){
                    minute_string = "0" + String.valueOf(minute);
                }




                setUpdateMsg("Alarm set to: " + month_string + "/" + day_string + "/"
                        +year_string + " "+ hour_string + ":" + minute_string);
                intent.putExtra("extra","on");

                // Create a pending intent that delays the intent
                // until the specified calendar time


                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);


                // Set the alarm manager
                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 6, pending_intent);
                calendar.add(Calendar.MONTH, 1);

                // The second alarm
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 1,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 6, pending_intent);
                calendar.add(Calendar.MONTH, 1);

                // The third alarm
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 2,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 6, pending_intent);


            }
        });



        // Initialize alarm off button
        final Button alarm_off = (Button) findViewById(R.id.alarm_off);
        // Create an onClick listener to stop the alarm
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Ringtone.isMusicOn){
                    setUpdateMsg("Alarm can't be canceled!");
                    count++;
                } else {
                    setUpdateMsg("Alarm off!");
                }


                alarmManager.cancel(pending_intent);
                intent.putExtra("extra", "off");

                sendBroadcast(intent);

            }
        });

        // initialize submit button
        final Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Ringtone.isMusicOn && !isCorrect){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Your answer is wrong, please try again!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();
                                    answer.setText("");
                                }
                            }
                    );

                    AlertDialog alert = builder.create();
                    alert.show();
                }

                alarmManager.cancel(pending_intent);
                intent.putExtra("extra", "submit");
                sendBroadcast(intent);
            }
        });


    }



    private TextWatcher answerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            givenAnswer = answer.getText().toString();

            if(givenAnswer.equals(Math.getSum()+"")){
                isCorrect = true;
                Log.e("Given answer is ", givenAnswer+"");
                Log.e("isCorrect is", isCorrect+"");
            }
        }
    };


    private TextWatcher onDaysTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            daysInterval = Integer.parseInt(days_on_num.getText().toString());

        }
    };

    public static void setUpdateMsg(String s) {
        update_msg.setText(s);
    }

    public static void setQuestionTV(String s){
        question.setText(s);
    }

    public static void reset(){
        setQuestionTV("^o^ v");
        answer.setText("");
        count = 0;
        isCorrect = false;
        setUpdateMsg("Alarm off!");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}