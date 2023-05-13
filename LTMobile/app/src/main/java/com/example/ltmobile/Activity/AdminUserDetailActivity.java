package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.ViewPagerAdapter;
import com.example.ltmobile.Fragment.ManagerAccountFragment;
import com.example.ltmobile.Fragment.ManagerInnsFragment;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserDetailActivity extends AppCompatActivity {

    Context context = this;

    //KEY
    public static final String KEY_EXTRA_USER = "KEY_EXTRA_USER";

    //variable
    int position;
    User user;

    //View
    ImageView imvAvt;
    TextView tvEmail, tvFullName, tvGender;
    Button btnLock;

    private TabLayout mTablayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_detail);

        connectView();
        getData();
        setEvent();

        setViewPager();
    }

    private void connectView(){
        imvAvt = findViewById(R.id.imvAvt);
        tvEmail = findViewById(R.id.tvEmail);
        tvFullName = findViewById(R.id.tvFullName);
        tvGender = findViewById(R.id.tvGender);
        btnLock = findViewById(R.id.btnLock);
        mTablayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
    }
    private void setViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                user.getUserId());

        mViewPager.setAdapter(viewPagerAdapter);

        mTablayout.setupWithViewPager(mViewPager);
    }

    private void setEvent(){
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceAPI.serviceapi.lockUser(user.getUserId())
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if(response.isSuccessful()) {
                                    Snackbar.make(v, "deleted", Snackbar.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(context, "An error occur", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("TAG", t.toString());
                                Toast.makeText(context, "connect server failed", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void getData(){
        position = getIntent().getIntExtra(KEY_EXTRA_USER, -1);
        user = ManagerAccountFragment.userList.get(position);

        tvEmail.setText(user.getEmail());
        tvFullName.setText(user.getFullname());
        tvGender.setText(user.getGender());

        Glide.with(context).load(user.getAvatar()).into(imvAvt);
    }
}