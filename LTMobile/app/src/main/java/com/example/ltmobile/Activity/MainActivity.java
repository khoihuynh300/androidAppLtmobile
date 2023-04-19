package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.ltmobile.R;

public class MainActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ai viết trong này thì có thể xóa 2 dòng phía dưới
        startActivity(new Intent(context, ProfileActivity.class));
        startActivity(new Intent(context, ChangePasswordActivity.class));
    }
}