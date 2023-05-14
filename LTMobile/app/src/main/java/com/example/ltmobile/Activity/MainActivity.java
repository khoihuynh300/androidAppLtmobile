package com.example.ltmobile.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.NavigationAdapter;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(context, ListInnActivity.class));
        if(!SharedPrefManager.getInstance(context).isLoggedIn()){
            finish();
            startActivity(new Intent(context, LoginActivity.class));
        }
        else if(SharedPrefManager.getInstance(context).getUser().getRole().equals("manager")){
            finish();
            startActivity(new Intent(context, AdminActivity.class));
        }

        // NAVIGATION
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        TextView txtTitle = findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                txtTitle.setText(destination.getLabel());
            }
        });

        View headerView = navigationView.getHeaderView(0);
        ImageView avatar = headerView.findViewById(R.id.imvAvatar);
        TextView fullname = headerView.findViewById(R.id.tvFullName);

        User user = SharedPrefManager.getInstance(context).getUser();
        fullname.setText(user.getFullname());
        Glide.with(context).load(user.getAvatar()).into(avatar);
        navigationView.getMenu().findItem(R.id.menuLogout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                SharedPrefManager.getInstance(context).logout();
                return false;
            }
        });
        navigationView.getMenu().findItem(R.id.menuInn).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(context, ListInnActivity.class);
                context.startActivity(intent);
                return false;
            }
        });
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationAdapter(this));
    }
}