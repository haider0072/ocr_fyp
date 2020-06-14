package com.example.finalyearproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String LOGIN = "login";
    private static final String USER_LOGIN = "userlogin";
    private static final String USER_NAME = "username";
    Context context;
    SharedPref(Context context) {
        this.context = context;
    }

    public void userLogin(String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGIN, "LOGGED");
        editor.putString(USER_NAME, name);
        editor.apply();
    }

    public void userLogOut(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGIN, "");
        editor.putString(USER_NAME, "");
        editor.apply();
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, "");
    }

    public Boolean isUserLogin(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(USER_LOGIN, "").equals("LOGGED"));
    }

}
