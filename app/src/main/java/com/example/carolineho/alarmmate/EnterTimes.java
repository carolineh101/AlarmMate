package com.example.carolineho.alarmmate;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

public class EnterTimes extends AppCompatActivity {

    private TextView mTimeM;
    private TextView mTimeT;
    private TextView mTimeW;
    private TextView mTimeTh;
    private TextView mTimeF;
    private Button mEditM;
    private Button mEditT;
    private Button mEditW;
    private Button mEditTh;
    private Button mEditF;
    private Button mSubmit;

    private DatabaseReference mDatabase;

    private String name;
    private String user;
    private String room;

    private int mHour;
    private int mMin;
    private int tHour;
    private int tMin;
    private int wHour;
    private int wMin;
    private int thHour;
    private int thMin;
    private int fHour;
    private int fMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_times);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTimeM = (TextView) findViewById(R.id.TimeM);
        mTimeT = (TextView) findViewById(R.id.TimeT);
        mTimeW = (TextView) findViewById(R.id.TimeW);
        mTimeTh = (TextView) findViewById(R.id.TimeTh);
        mTimeF = (TextView) findViewById(R.id.TimeF);
        mEditM = (Button) findViewById(R.id.EditM);
        mEditT = (Button) findViewById(R.id.EditT);
        mEditW = (Button) findViewById(R.id.EditW);
        mEditTh = (Button) findViewById(R.id.EditTh);
        mEditF = (Button) findViewById(R.id.EditF);
        mSubmit = (Button) findViewById(R.id.Submit);

        name = "";
        user = "";
        room = "";

        mHour = 0;
        mMin = 0;
        tHour = 0;
        tMin = 0;
        wHour = 0;
        wMin = 0;
        thHour = 0;
        thMin = 0;
        fHour = 0;
        fMin = 0;

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            name = (String) bd.get("name");
            user = (String) bd.get("user");
            room = (String) bd.get("room");
        }

        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EnterTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ampm = " AM";
                        String potentialZero = "";
                        int hour = selectedHour;
                        String minutes = "" + selectedMinute;
                        if (selectedHour >= 12) {
                            if (selectedHour > 12) {
                                hour -= 12;
                            }
                            ampm = " PM";
                        } else if (selectedHour == 0) {
                            hour = 12;
                        }
                        if (selectedMinute < 10) {
                            potentialZero = "0";
                        }
                        mTimeM.setText(hour + ":" + potentialZero + minutes + ampm);
                        mHour = selectedHour;
                        mMin = selectedMinute;
                        ArrayList<Integer> days = new ArrayList<>();
                        days.add(0, MONDAY);
                        Intent mAlarm = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_HOUR, mHour)
                                .putExtra(AlarmClock.EXTRA_MINUTES, mMin)
                                .putExtra(AlarmClock.EXTRA_DAYS, days)
                                .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                                .putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (mAlarm.resolveActivity(getPackageManager()) != null) {
                            startActivity(mAlarm);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        mEditM.setOnClickListener(mListener);

        View.OnClickListener tListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EnterTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ampm = " AM";
                        String potentialZero = "";
                        int hour = selectedHour;
                        String minutes = "" + selectedMinute;
                        if (selectedHour >= 12) {
                            if (selectedHour > 12) {
                                hour -= 12;
                            }
                            ampm = " PM";
                        } else if (selectedHour == 0) {
                            hour = 12;
                        }
                        if (selectedMinute < 10) {
                            potentialZero = "0";
                        }
                        mTimeT.setText(hour + ":" + potentialZero + minutes + ampm);
                        tHour = selectedHour;
                        tMin = selectedMinute;
                        ArrayList<Integer> days = new ArrayList<>();
                        days.add(0, TUESDAY);
                        Intent tAlarm = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_HOUR, tHour)
                                .putExtra(AlarmClock.EXTRA_MINUTES, tMin)
                                .putExtra(AlarmClock.EXTRA_DAYS, days)
                                .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                                .putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (tAlarm.resolveActivity(getPackageManager()) != null) {
                            startActivity(tAlarm);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        mEditT.setOnClickListener(tListener);

        View.OnClickListener wListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EnterTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ampm = " AM";
                        String potentialZero = "";
                        int hour = selectedHour;
                        String minutes = "" + selectedMinute;
                        if (selectedHour >= 12) {
                            if (selectedHour > 12) {
                                hour -= 12;
                            }
                            ampm = " PM";
                        } else if (selectedHour == 0) {
                            hour = 12;
                        }
                        if (selectedMinute < 10) {
                            potentialZero = "0";
                        }
                        mTimeW.setText(hour + ":" + potentialZero + minutes + ampm);
                        wHour = selectedHour;
                        wMin = selectedMinute;
                        ArrayList<Integer> days = new ArrayList<>();
                        days.add(0, WEDNESDAY);
                        Intent wAlarm = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_HOUR, wHour)
                                .putExtra(AlarmClock.EXTRA_MINUTES, wMin)
                                .putExtra(AlarmClock.EXTRA_DAYS, days)
                                .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                                .putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (wAlarm.resolveActivity(getPackageManager()) != null) {
                            startActivity(wAlarm);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        mEditW.setOnClickListener(wListener);

        View.OnClickListener thListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EnterTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ampm = " AM";
                        String potentialZero = "";
                        int hour = selectedHour;
                        String minutes = "" + selectedMinute;
                        if (selectedHour >= 12) {
                            if (selectedHour > 12) {
                                hour -= 12;
                            }
                            ampm = " PM";
                        } else if (selectedHour == 0) {
                            hour = 12;
                        }
                        if (selectedMinute < 10) {
                            potentialZero = "0";
                        }
                        mTimeTh.setText(hour + ":" + potentialZero + minutes + ampm);
                        thHour = selectedHour;
                        thMin = selectedMinute;
                        ArrayList<Integer> days = new ArrayList<>();
                        days.add(0, THURSDAY);
                        Intent thAlarm = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_HOUR, thHour)
                                .putExtra(AlarmClock.EXTRA_MINUTES, thMin)
                                .putExtra(AlarmClock.EXTRA_DAYS, days)
                                .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                                .putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (thAlarm.resolveActivity(getPackageManager()) != null) {
                            startActivity(thAlarm);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        mEditTh.setOnClickListener(thListener);

        View.OnClickListener fListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EnterTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ampm = " AM";
                        String potentialZero = "";
                        int hour = selectedHour;
                        String minutes = "" + selectedMinute;
                        if (selectedHour >= 12) {
                            if (selectedHour > 12) {
                                hour -= 12;
                            }
                            ampm = " PM";
                        } else if (selectedHour == 0) {
                            hour = 12;
                        }
                        if (selectedMinute < 10) {
                            potentialZero = "0";
                        }
                        mTimeF.setText(hour + ":" + potentialZero + minutes + ampm);
                        fHour = selectedHour;
                        fMin = selectedMinute;
                        ArrayList<Integer> days = new ArrayList<>();
                        days.add(0, FRIDAY);
                        Intent fAlarm = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_HOUR, fHour)
                                .putExtra(AlarmClock.EXTRA_MINUTES, fMin)
                                .putExtra(AlarmClock.EXTRA_DAYS, days)
                                .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                                .putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (fAlarm.resolveActivity(getPackageManager()) != null) {
                            startActivity(fAlarm);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        mEditF.setOnClickListener(fListener);

        View.OnClickListener submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("rooms").child(room).child("user" + user).child("monday").setValue(mTimeM.getText().toString());
                mDatabase.child("rooms").child(room).child("user" + user).child("tuesday").setValue(mTimeT.getText().toString());
                mDatabase.child("rooms").child(room).child("user" + user).child("wednesday").setValue(mTimeW.getText().toString());
                mDatabase.child("rooms").child(room).child("user" + user).child("thursday").setValue(mTimeTh.getText().toString());
                mDatabase.child("rooms").child(room).child("user" + user).child("friday").setValue(mTimeF.getText().toString());
                mDatabase.child("rooms").child(room).child("user" + user).child("status").setValue("awake");

                Intent intent = new Intent(EnterTimes.this, ScheduleStatus.class);
                intent.putExtra("user", user);
                intent.putExtra("room", room);
                startActivity(intent);
            }
        };

        mSubmit.setOnClickListener(submitListener);
    }

}
