package com.example.carolineho.alarmmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class SignIn extends AppCompatActivity {

    private Firebase myFirebaseRef;
    private Button mSignUp;
    private EditText mSignUpEmail;
    private EditText mSignUpPassword;
    //private Boolean mOpenedBefore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSignUp = (Button) findViewById(R.id.SignUp);
        mSignUpEmail = (EditText) findViewById(R.id.SignUpEmail);
        mSignUpPassword = (EditText) findViewById(R.id.SignUpPassword);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://alarmmate-fa8d9.firebaseio.com/");

        View.OnClickListener signUpListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mOpenedBefore = true;

                myFirebaseRef.createUser(mSignUpEmail.getText().toString(), mSignUpPassword.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });

                myFirebaseRef.authWithPassword(mSignUpEmail.getText().toString(), mSignUpPassword.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });

                Intent intent = new Intent(SignIn.this, CreateRoom.class);
                startActivity(intent);
            }
        };

        mSignUp.setOnClickListener(signUpListener);
    }
}
