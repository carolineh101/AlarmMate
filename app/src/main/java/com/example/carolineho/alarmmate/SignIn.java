package com.example.carolineho.alarmmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignIn extends AppCompatActivity {

    private Button mSignUp;
    private EditText mSignUpName;
    private EditText mSignUpEmail;
    private EditText mSignUpPassword;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSignUp = (Button) findViewById(R.id.SignUp);
        mSignUpName = (EditText) findViewById(R.id.SignUpName);
        mSignUpEmail = (EditText) findViewById(R.id.SignUpEmail);
        mSignUpPassword = (EditText) findViewById(R.id.SignUpPassword);


        View.OnClickListener signUpListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("users").child(mSignUpName.getText().toString()).child("name").setValue(mSignUpName.getText().toString());
                mDatabase.child("users").child(mSignUpName.getText().toString()).child("email").setValue(mSignUpEmail.getText().toString());
                mDatabase.child("users").child(mSignUpName.getText().toString()).child("password").setValue(mSignUpPassword.getText().toString());

                Intent intent = new Intent(SignIn.this, CreateRoom.class);
                intent.putExtra("name", mSignUpName.getText().toString());
                intent.putExtra("email", mSignUpEmail.getText().toString());
                startActivity(intent);
            }
        };

        mSignUp.setOnClickListener(signUpListener);
    }
}
