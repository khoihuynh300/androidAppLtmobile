package com.example.ltmobile.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.ltmobile.Activity.LoginActivity;
import com.example.ltmobile.Model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_ID = "keyid";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_FULLNAME = "keyfullname";
    private static final String KEY_AVATAR = "keyavatar";
    private static final String KEY_ROLE = "keyrole";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if(mInstance==null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getUserId());
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_FULLNAME,user.getFullname());
        editor.putString(KEY_AVATAR,user.getAvatar());
        editor.putString(KEY_ROLE, user.getRole());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID,-1),
                sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_FULLNAME,null),
                sharedPreferences.getString(KEY_AVATAR,null),
                sharedPreferences.getString(KEY_ROLE,null)
        );
    }

    public void logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }
}
