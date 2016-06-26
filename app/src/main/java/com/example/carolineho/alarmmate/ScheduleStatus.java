package com.example.carolineho.alarmmate;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ScheduleStatus extends AppCompatActivity {

    private Button mMySleepStatus;
    private Button mDisable;
    private Button mSnooze;
    private TextView mTheirSleepStatus;
    private TextView mMyAlarm;
    private TextView mTheirAlarm;

    private DatabaseReference mDatabase;

    private String user;
    private String room;
    private String other;
    private String theirName;

    private String myMilt;
    private String theirMilt;

    private Calendar mCurrentTime;
    private int day;
    private String dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_status);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mMySleepStatus = (Button) findViewById(R.id.MySleepStatus);
        mDisable = (Button) findViewById(R.id.Disable);
        mSnooze = (Button) findViewById(R.id.Snooze);
        mMyAlarm = (TextView) findViewById(R.id.MyAlarm);
        mTheirAlarm = (TextView) findViewById(R.id.TheirAlarm);
        mTheirSleepStatus = (TextView) findViewById(R.id.TheirSleepStatus);

        user = "";
        room = "";
        other = "";
        theirName = "";
        myMilt = "";
        theirMilt = "";

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            user = (String) bd.get("user");
            room = (String) bd.get("room");
        }
        if (user.equals("1")) {
            other = "2";
        } else {
            other = "1";
        }

        mCurrentTime = Calendar.getInstance();
        day = mCurrentTime.get(Calendar.DAY_OF_WEEK);
        if(day==1 || day==6 || day==7){
            dayOfWeek="monday";
        }
        else if(day==2){
            dayOfWeek="tuesday";
        }
        else if(day==3){
            dayOfWeek="wednesday";
        }
        else if(day==4){
            dayOfWeek="thursday";
        }
        else if(day==5){
            dayOfWeek="friday";
        }

        mDatabase.child("rooms").child(room).child("user" + other).child("name").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String otherName = (String) dataSnapshot.getValue();
                        mTheirSleepStatus.setText(otherName + " is awake");
                        theirName = otherName;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", "getUser:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("rooms").child(room).child("user" + user).child(dayOfWeek).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String myTime = (String) dataSnapshot.getValue();
                        mMyAlarm.setText("Your alarm is set for " + myTime);
                        if (myTime.substring(myTime.length()-2, myTime.length()).equals("AM")) {
                            if (myTime.substring(0, 2).equals("12")) {

                            } else {
                                myMilt += myTime.substring(0, 1);
                            }
                        } else {
                            int parseOrigHour = Integer.parseInt(myTime.substring(0, 1)) + 12;
                            myMilt += parseOrigHour;
                        }
                        myMilt += myTime.substring(myTime.indexOf(":") + 1, myTime.indexOf(":") + 3);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", "getUser:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("rooms").child(room).child("user" + other).child(dayOfWeek).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String theirTime = (String) dataSnapshot.getValue();
                        mTheirAlarm.setText(theirName + "'s alarm is set for " + theirTime);
                        if (theirTime.substring(theirTime.length()-2, theirTime.length()).equals("AM")) {
                            if (theirTime.substring(0, 2).equals("12")) {

                            } else {
                                theirMilt += theirTime.substring(0, 1);
                            }
                        } else {
                            int parseOrigHour = Integer.parseInt(theirTime.substring(0, 1)) + 12;
                            theirMilt += parseOrigHour;
                        }
                        theirMilt += theirTime.substring(theirTime.indexOf(":") + 1, theirTime.indexOf(":") + 3);
                        int myParse = Integer.parseInt(myMilt);
                        int theirParse = Integer.parseInt(theirMilt);
                        if (myParse > theirParse) {
                            mSnooze.setVisibility(View.VISIBLE);
                        } else {
                            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING, 7, 0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", "getUser:onCancelled", databaseError.toException());
                    }
                });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Changed", "onChildChanged:" + dataSnapshot.getKey());
                String status = (String) dataSnapshot.getValue();
                if (status.equals("awake")) {
                    mTheirSleepStatus.setText(theirName + " is awake");
                } else {
                    mTheirSleepStatus.setText(theirName + " is asleep");
                }
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                //
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                //
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Cancelled", "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ScheduleStatus.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase.child("rooms").child(room).child("user" + other).child("status").addChildEventListener(childEventListener);

        View.OnClickListener sleepListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("I am going to sleep")) {
                    b.setText("I am awake");
                    mDatabase.child("rooms").child(room).child("user" + user).child("status").setValue("awake");
                } else {
                    b.setText("I am asleep");
                    mDatabase.child("rooms").child(room).child("user" + user).child("status").setValue("asleep");
                }
            }
        };

        mMySleepStatus.setOnClickListener(sleepListener);

        View.OnClickListener disableListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMySleepStatus.setText("I am awake");
                mDatabase.child("rooms").child(room).child("user" + user).child("status").setValue("awake");
                Intent intent = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
                intent.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_NEXT);
                startActivity(intent);
            }
        };

        mDisable.setOnClickListener(disableListener);

        View.OnClickListener snoozeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMySleepStatus.setText("I am awake");
                mDatabase.child("rooms").child(room).child("user" + user).child("status").setValue("awake");
                Intent intent = new Intent(AlarmClock.ACTION_SNOOZE_ALARM);
                startActivity(intent);
            }
        };

        mDisable.setOnClickListener(snoozeListener);
    }
}
