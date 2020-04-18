package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private EditText myusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputUsername = findViewById(R.id.username);
        textInputPassword = findViewById(R.id.password);
        myusername = findViewById(R.id.usernametextfield);
        final EditText mypassword = findViewById(R.id.passwordtextfield);

        Button signin = (Button) findViewById(R.id.signin_Button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validemail = "^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$";



                String email = myusername.getText().toString();
                Matcher matcher= Pattern.compile(validemail).matcher(email);


                if (matcher.matches() && isValidPassword(mypassword.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(v.getContext(),result_activity.class);
                    startActivityForResult(myIntent,0);


                }

                else if (!matcher.matches()){
                    myusername.setError("example: abc@abc.com");

                }
                else if (!isValidPassword(mypassword.getText().toString().trim())){
                    mypassword.setError("At least one numeric character");

                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter Valid Email-Id",Toast.LENGTH_LONG).show();
                }
            }

        });

        TextView signup = (TextView) findViewById(R.id.signupto);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),signup.class);
                startActivityForResult(myIntent,0);


        }

        });

        }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher1;

        final String PASSWORD_PATTERN = "^(?=.*\\d).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher1 = pattern.matcher(password);

        return matcher1.matches();

    }



    }

