package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    private EditText username;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText myname = (EditText) findViewById(R.id.myname);
        Button signup = (Button) findViewById(R.id.signupButton);
        final EditText password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        name = findViewById(R.id.myname);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validemail = "^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$";
                String validname = "^[a-zA-Z]\\w{0,}$";



                String email = username.getText().toString();
                String firstname = myname.getText().toString();
                Matcher matcher= Pattern.compile(validemail).matcher(email);
                Matcher matcher1= Pattern.compile(validname).matcher(firstname);


                if (matcher.matches() && isValidPassword(password.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(v.getContext(),result_activity.class);
                    startActivityForResult(myIntent,0);


                }
                else if (!matcher.matches()){
                    username.setError("example: abc@abc.com");

                }
                else if (!isValidPassword(password.getText().toString().trim())){
                    password.setError("At least one numeric character");

                }
                else if (!matcher1.matches()){
                    myname.setError("This Field Is Empty");

                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter Valid Email-Id",Toast.LENGTH_LONG).show();
                }
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
