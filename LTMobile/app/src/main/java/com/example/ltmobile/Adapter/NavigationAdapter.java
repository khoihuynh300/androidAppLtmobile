package com.example.ltmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.ltmobile.Activity.ListInnActivity;
import com.example.ltmobile.Activity.LoginActivity;
import com.example.ltmobile.Activity.MainActivity;
import com.example.ltmobile.Activity.ProfileActivity;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class NavigationAdapter implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;

    public NavigationAdapter(Context context) {
        this.context = context;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        User user = SharedPrefManager.getInstance(context).getUser();
        Intent intent;
        switch (item.getItemId()) {
//            case R.id.menuHome:
//                intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
//                return true;
            case R.id.menuProfile:
                intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
                return true;
            case R.id.menuQA:

                return true;
            case R.id.menuInn:
                intent = new Intent(context, ListInnActivity.class);
                context.startActivity(intent);
                return true;
            case R.id.menuLogout:
                SharedPrefManager.getInstance(context).logout();
                return true;
            default:
                return false;
        }
    }
}
