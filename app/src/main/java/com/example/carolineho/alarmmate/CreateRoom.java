package com.example.carolineho.alarmmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRoom extends AppCompatActivity {

    private Button mJoin;
    private Button mCreate;
    private EditText mNewRoom;
    private EditText mRoommateEmail;
    private EditText mRoomName;
    private DatabaseReference mDatabase;
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mJoin = (Button) findViewById(R.id.Join);
        mCreate = (Button) findViewById(R.id.Create);
        mNewRoom = (EditText) findViewById(R.id.NewRoom);
        mRoommateEmail = (EditText) findViewById(R.id.RoommateEmail);
        mRoomName = (EditText) findViewById(R.id.RoomName);
        name = "";
        email = "";

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            name = (String) bd.get("name");
            email = (String) bd.get("email");
        }

        View.OnClickListener newRoomListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("rooms").child(mNewRoom.getText().toString()).setValue(mNewRoom.getText().toString());
                mDatabase.child("rooms").child((mNewRoom.getText().toString())).child("user1").child("name").setValue(name);
                mDatabase.child("rooms").child((mNewRoom.getText().toString())).child("user1").child("email").setValue(email);
                mDatabase.child("rooms").child((mNewRoom.getText().toString())).child("user2").child("email").setValue(mRoommateEmail.getText().toString());

                Intent intent = new Intent(CreateRoom.this, EnterTimes.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        };

        mCreate.setOnClickListener(newRoomListener);

        View.OnClickListener joinRoomListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (mDatabase.child("rooms").child((mRoomName.getText().toString())).child("user2").child("email").getValue().equals(email)) {
                    mDatabase.child("rooms").child((mRoomName.getText().toString())).child("user2").child("name").setValue(name);
                //}

                Intent intent = new Intent(CreateRoom.this, EnterTimes.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        };

        mJoin.setOnClickListener(joinRoomListener);
    }
}
