package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.ViewPagerAdapter;
import com.example.ltmobile.Fragment.ManagerAccountFragment;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {

    Context context = this;
    //KEY
    public static final String KEY_EXTRA_USER = "KEY_EXTRA_USER";

    //variable
    int userId;
    User user;

    //View
    ImageView imvAvt;
    TextView tvEmail, tvFullName, tvGender;

    private TabLayout mTablayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        connectView();
        getData();
    }

    private void connectView(){
        imvAvt = findViewById(R.id.imvAvt);
        tvEmail = findViewById(R.id.tvEmail);
        tvFullName = findViewById(R.id.tvFullName);
        tvGender = findViewById(R.id.tvGender);
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

    private void getData(){
        userId = getIntent().getIntExtra(KEY_EXTRA_USER, -1);
//        user = ManagerAccountFragment.userList.get(position);
        // call api get user
        ServiceAPI.serviceapi.getUserById(userId)
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if(response.isSuccessful()){
                                    JsonObject userObject = response.body().get("result").getAsJsonObject();
                                    user = new User(
                                            userObject.get("userId").getAsInt(),
                                            userObject.get("email").getAsString(),
                                            userObject.get("fullname").getAsString(),
                                            userObject.get("gender").getAsString(),
                                            userObject.get("avatar").getAsString(),
                                            userObject.get("role").getAsString());
                                    tvEmail.setText(user.getEmail());
                                    tvFullName.setText(user.getFullname());
                                    tvGender.setText(user.getGender());

                                    Glide.with(context).load(user.getAvatar()).into(imvAvt);


                                    setViewPager();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("TAG", t.toString());
                                Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
                            }
                        });
    }
}