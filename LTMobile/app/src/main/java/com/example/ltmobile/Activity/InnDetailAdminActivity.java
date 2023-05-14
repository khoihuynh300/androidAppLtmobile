package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltmobile.Adapter.SliderAdapter;
import com.example.ltmobile.Fragment.ManagerInnsFragment;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InnDetailAdminActivity extends AppCompatActivity {
    Context context = this;
    public static final String KEY_EXTRA_INN = "KEY_EXTRA_INN";

    private TextView txtAddress,txtPriceElec ,txtPriceWater ,txtSize, txtPhone, txtPrice, txtDesc;
    private Button btnConfirm;
    private Button btnDelete;

    private SliderView sliderView;
    private List<ImageInn> imageList;
    SliderAdapter sliderAdapter;

    Inn innObject;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inn_detail_admin);

        imageList = new ArrayList<>();
        connectView();
        // get data from previous activity
        position = getIntent().getIntExtra(KEY_EXTRA_INN, -1);
        innObject = ManagerInnsFragment.innList.get(position);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            innObject = (Inn) bundle.getSerializable(KEY_EXTRA_INN);
            txtAddress.setText(innObject.getAddress());
            txtPhone.setText(innObject.getPhoneNumber());
            txtPrice.setText(String.valueOf(innObject.getPrice()));
            txtDesc.setText(innObject.getDescribe());
            txtPriceElec.setText(innObject.getPriceELec().toString());
            txtPriceWater.setText(innObject.getPriceWater().toString());
            txtSize.setText(String.valueOf(innObject.getSize()));
            imageList = innObject.getImages();
            sliderAdapter = new SliderAdapter(getApplicationContext(), imageList);
//            sliderAdapter = new SliderAdapter(getApplicationContext(), imageList);
            sliderView.setSliderAdapter(sliderAdapter);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            sliderView.setIndicatorSelectedColor(Color.RED);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.startAutoCycle();
            sliderView.setScrollTimeInSec(5);
//        }

        setEvent();
    }

    private void connectView(){
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        txtPrice = findViewById(R.id.txtPrice);
        txtDesc = findViewById(R.id.txtDesc);
        sliderView = findViewById(R.id.imageSlider);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnDelete = findViewById(R.id.btnDelete);
        txtPriceElec = findViewById(R.id.txtPriceElec);
        txtPriceWater = findViewById(R.id.txtPriceWater);
        txtSize = findViewById(R.id.txtSize);
    }
    private void setEvent(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceAPI.serviceapi.confirmInn(innObject.getInnId(), SharedPrefManager.getInstance(context).getUser().getUserId())
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if(response.isSuccessful()) {
                                    Snackbar.make(v, "confirmed", Snackbar.LENGTH_LONG).show();
                                    ManagerInnsFragment.innList.get(position).setConfirmed(true);
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceAPI.serviceapi.deleteInn(innObject.getInnId())
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if(response.isSuccessful()) {
                                    Snackbar.make(v, "deleted", Snackbar.LENGTH_LONG).show();
                                    ManagerInnsFragment.innList.remove(ManagerInnsFragment.innList.get(position));
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
}