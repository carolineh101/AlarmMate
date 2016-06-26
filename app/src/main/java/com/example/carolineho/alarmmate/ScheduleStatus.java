package com.example.carolineho.alarmmate;

import android.content.Intent;
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

public class ScheduleStatus extends AppCompatActivity {

    private Button mMySleepStatus;
    private TextView mTheirSleepStatus;

    private DatabaseReference mDatabase;

    private String name;
    private String user;
    private String room;
    private String other;
    private String theirName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_status);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mMySleepStatus = (Button) findViewById(R.id.MySleepStatus);
        mTheirSleepStatus = (TextView) findViewById(R.id.TheirSleepStatus);

        name = "";
        user = "";
        room = "";
        other = "";
        theirName = "";

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            name = (String) bd.get("name");
            user = (String) bd.get("user");
            room = (String) bd.get("room");
        }
        if (user.equals("1")) {
            other = "2";
        } else {
            other = "1";
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
    }
}
