package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myref;

    EditText userL,pwdL;
    Button signIn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userL = (EditText) findViewById(R.id.username);
        pwdL = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.signin_Button);

        database=FirebaseDatabase.getInstance();
        myref=database.getReference("Admins");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validemail = "^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$";

                final String logUsername = userL.getText().toString();
                final String logPassword = pwdL.getText().toString();

                if (TextUtils.isEmpty(logUsername) || TextUtils.isEmpty(logPassword)){
                    if (TextUtils.isEmpty(logUsername)){
                        userL.setError("Field is Empty");
                    }
                    if (TextUtils.isEmpty(logPassword)){
                        pwdL.setError("Field is Empty");
                    }
                }
                else{
                    LoginClass lc=new LoginClass(login.this);
                    lc.LoginAsUser(logUsername,logPassword);
//                  Toast.makeText(Login.this,"USERR",Toast.LENGTH_SHORT).show();

                }
            }

     });

        TextView signUp = (TextView) findViewById(R.id.signupto);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),signup.class);
                startActivityForResult(myIntent,0);

            }

        });

    }
    @Override
    public void onStart() {
        super.onStart();
//         Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null) {
            startActivity(new Intent(getApplicationContext(), result_activity.class));
            finish();
        }

    }


//    protected void onStart() {
//        super.onStart();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//    }

}

