package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
//    private EditText name;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText name = (EditText) findViewById(R.id.myname);
        final EditText password = findViewById(R.id.password);
        final EditText username = findViewById(R.id.username);

        final Button b_signup = (Button) findViewById(R.id.signupButton);

        //init firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        //OnClickListener On Button

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailRegex = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";
                String email = username.getText().toString();
                String pwd = password.getText().toString();
                final String uname = name.getText().toString();
                if(email.isEmpty()){
                    username.setError("Please Enter username");
                    username.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please Enter password");
                    password.requestFocus();
                }

                else if(uname.isEmpty()){
                    name.setError("Please Enter name");
                    name.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty() && uname.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_LONG).show();
                }

                else if(!email.isEmpty() && pwd.isEmpty() && uname.isEmpty() && email.matches(emailRegex)){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        HashMap<String,String> mp = new HashMap<>();
                                        mp.put("name",uname);
                                        FirebaseUser userID=mFirebaseAuth.getCurrentUser();
                                        databaseReference.child("Users").child(userID.getUid()).setValue(mp);
                                        startActivity(new Intent(signup.this,login.class));
                                        Toast.makeText(getBaseContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getBaseContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                }

            }


        });
    }

//    public boolean isValidPassword(final String password) {
//
//        Pattern pattern;
//        Matcher matcher1;
//
//        final String PASSWORD_PATTERN = "^(?=.*\\d).{4,}$";
//
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher1 = pattern.matcher(password);
//
//        return matcher1.matches();
//
//    }
}
