package com.example.finalyearproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginClass {
    FirebaseDatabase database;
    Context context;
    boolean flag=false;
    DatabaseReference myref;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener FirebaseAuthLstener;
    public LoginClass(Context c) {
        this.context=c;
    }

    public void LoginAsUser(String email, String password){
        mAuth=FirebaseAuth.getInstance(); //it will store status if someone is logged in or out signed in or out
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Intent intent=new Intent(context, MainActivity.class);
//                            startActivity(intent);
                            Toast.makeText(context,"Successfully Login ",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(context,result_activity.class);
                            context.startActivity(i);
                            ((Activity) context).finish();

                        } else {
                            Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_LONG).show();
//                            task.getException();
                        }

                        // ...
                    }
                });
    }
}
