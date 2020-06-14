package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference databaseReference_user;

    TextView name, email, phone, username, location, role;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profile_fname);
        email= findViewById(R.id.profile_email);

        // Write a message to the database
        //getting firebase uthentication instance
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
//        showMessage(userID);

        databaseReference_user = database.getReference("Users");
        // Read from the database
        databaseReference_user.addValueEventListener(new ValueEventListener() {
            final String[] data = new String[7];
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
////                Log.d(TAG, "Value is: " + value);
//                showMessage(value);
                data[0]= dataSnapshot.child(userID).child("Name").getValue(String.class);
                data[1]= FirebaseAuth.getInstance().getCurrentUser().getEmail();



                    name.setText(data[0]);
                    email.setText(data[1]);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());

                showMessage(error.toException().toString());
            }
        });
    }

    public void profile_Singout(View view) {
        FirebaseAuth.getInstance().signOut();
        new SharedPref(this).userLogOut();
        Intent loginActivity = new Intent(getApplicationContext(), login.class);
        startActivity(loginActivity);
        finish();
    }
    void showMessage(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}