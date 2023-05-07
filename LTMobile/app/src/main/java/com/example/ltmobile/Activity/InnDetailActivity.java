package com.example.ltmobile.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltmobile.Adapter.InnAdapter;
import com.example.ltmobile.Adapter.InnTabAdapter;
import com.example.ltmobile.Fragment.ImageInnFragment;
import com.example.ltmobile.Fragment.InnFragment;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InnDetailActivity extends AppCompatActivity{
    Toolbar toolbar;
    TabLayout tabLayout;
    List<Fragment> fragments = new ArrayList<>();
    InnAdapter innAdapter;
    DrawerLayout navigationDrawer;
    LinearLayout contactNow;
    NavigationView navigationView;
    RelativeLayout btnNavigation;
    ViewPager2 viewInn, viewListInn;
    InnTabAdapter innTabAdapter;
    TextView some_id, ubud_indone, pricetxt;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inn_detail);
        int innId = getIntent().getIntExtra("innId", 0);
        String des = getIntent().getStringExtra("Describe");

        mapping();
        getInnDetail(innId);

        FragmentManager manager = getSupportFragmentManager();
        innTabAdapter = new InnTabAdapter(manager, getLifecycle(), innId, des);
        viewInn.setAdapter(innTabAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewInn.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewInn.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationDrawer.openDrawer(GravityCompat.END);
            }
        });

        contactNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone; // Số điện thoại cần gọi
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationDrawer = (DrawerLayout) findViewById(R.id.navigationDrawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        btnNavigation = (RelativeLayout) findViewById(R.id.btnNavigation);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewInn = (ViewPager2) findViewById(R.id.viewInn);
        viewListInn = (ViewPager2) findViewById(R.id.viewListInn);
        contactNow = (LinearLayout) findViewById(R.id.contactNow);
        pricetxt = (TextView) findViewById(R.id.pricetxt);
        some_id = (TextView) findViewById(R.id.some_id);
        ubud_indone = (TextView) findViewById(R.id.ubud_indone);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("San Pham");
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));

    }

    private void getInnDetail(int innId) {
        ServiceAPI.serviceapi.getInnById(innId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int id = jsonObject.getInt("innId");
                        String address = jsonObject.getString("address");
                        String phoneNumber = jsonObject.getString("phoneNumber");
                        String describe = jsonObject.getString("describe");
                        Double price = jsonObject.getDouble("price");
                        String createdAtString = jsonObject.getString("createdAt");
                        String updatedAtString = jsonObject.getString("updatedAt");
                        JSONArray images = jsonObject.getJSONArray("images");
                        Date createdAt = null;
                        Date updatedAt = null;
                        try{
                            createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAtString);
                            updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(updatedAtString);
                        } catch (ParseException e) {
                            Log.e("TAG", e.toString());
                        }

                        List<ImageInn> imageInns = new ArrayList<>();
                        for(int i=0; i<images.length(); i++)
                        {
                            JSONObject image = images.getJSONObject(i);
                            String val = image.getString("image");
                            fragments.add(ImageInnFragment.newInstance(getApplicationContext(), val));
                        }
                        pricetxt.setText(price.toString());
                        ubud_indone.setText(address);
                        phone = phoneNumber;
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    innAdapter = new InnAdapter(InnDetailActivity.this, fragments);
                    viewListInn.setAdapter(innAdapter);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getApplicationContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }
}