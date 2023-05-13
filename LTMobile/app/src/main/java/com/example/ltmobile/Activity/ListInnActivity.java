package com.example.ltmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.InnAdapter;
import com.example.ltmobile.Fragment.InnFragment;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListInnActivity extends AppCompatActivity {

    private String[] location = {"Quan 1", "Quan 2", "Quan 3", "Quan 4", "Quan 5", "Quan 6", "Quan 7", "Quan 8", "Quan 9"};
    private String[] price = {"1M - 1M5", "1M5 - 2M", "2M - 3M", "Tren 3M"};
    private Object[] searchText = new Object[]{"address", 0, 0, 2};
    private int person = 2;
    private int innId;
    private String des;
    List<Inn> inns = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    InnAdapter innAdapter;
    TextView txtlocation, txtprice, txtpersion;
    AutoCompleteTextView autoCompleteLocation, autoCompletePrice;
    ArrayAdapter<String> adapterLocation, adapterPrice;
    RelativeLayout filterMinus, filterPlus, btnBack, btnNext, continue_bu, btnLearnmore, avatar;
    ViewPager2 viewListInn;
    NavigationView navigationView;
    DrawerLayout navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_inn);

        mapping();
        adapter();
        addInn();

        //Events
        autoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                searchText[0] = item;
                txtlocation.setText(item);
            }
        });

        autoCompletePrice.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                switch (item) {
                    case "1M - 1M5":
                        searchText[1] = 1000000;
                        searchText[2] = 1500000;
                        break;
                    case "1M5 - 2M":
                        searchText[1] = 1500000;
                        searchText[2] = 2000000;
                        break;
                    case "2M - 3M":
                        searchText[1] = 2000000;
                        searchText[2] = 3000000;
                        break;
                    case "Tren 3M":
                        searchText[1] = 3000000;
                        searchText[2] = 1000000000;
                        break;
                }
                txtprice.setText(item);
            }
        });

        filterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(person > 1)
                {
                    person--;
                    searchText[3] = person;
                    txtpersion.setText(String.valueOf(person) + " Person");
                }
            }
        });

        filterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(person < 5)
                {
                    person++;
                    searchText[3] = person;
                    txtpersion.setText(String.valueOf(person) + " Person");
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewListInn.getCurrentItem() < fragments.size()) {
                    viewListInn.setCurrentItem(viewListInn.getCurrentItem() + 1);
                    innId = inns.get(viewListInn.getCurrentItem()).getInnId();
                    des = inns.get(viewListInn.getCurrentItem()).getDescribe();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewListInn.getCurrentItem() > 0) {
                    viewListInn.setCurrentItem(viewListInn.getCurrentItem() - 1);
                    innId = inns.get(viewListInn.getCurrentItem()).getInnId();
                    des = inns.get(viewListInn.getCurrentItem()).getDescribe();
                }
            }
        });

        continue_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInn();
            }
        });

        btnLearnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InnDetailActivity.class);
                intent.putExtra("innId", innId);
                intent.putExtra("Describe", des);
                startActivity(intent);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationDrawer.openDrawer(GravityCompat.END);
            }
        });
    }

    private void mapping() {
        txtlocation = (TextView) findViewById(R.id.txtlocation);
        txtprice = (TextView) findViewById(R.id.txtprice);
        txtpersion = (TextView) findViewById(R.id.txtpersion);
        autoCompleteLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocation);
        autoCompletePrice = (AutoCompleteTextView) findViewById(R.id.autoCompletePrice);
        filterMinus = (RelativeLayout) findViewById(R.id.filterMinus);
        filterPlus = (RelativeLayout) findViewById(R.id.filterPlus);
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        btnNext = (RelativeLayout) findViewById(R.id.btnNext);
        btnLearnmore = (RelativeLayout) findViewById(R.id.btnLearnmore);
        continue_bu = (RelativeLayout) findViewById(R.id.continue_bu);
        viewListInn = (ViewPager2) findViewById(R.id.viewListInn);
        navigationDrawer = (DrawerLayout) findViewById(R.id.navigationDrawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        avatar = (RelativeLayout) findViewById(R.id.avatar);

        viewListInn.setUserInputEnabled(false);
    }

    private void adapter() {
        adapterLocation = new ArrayAdapter<String>(this, R.layout.list_item, location);
        adapterPrice = new ArrayAdapter<String>(this, R.layout.list_item, price);
        autoCompleteLocation.setAdapter(adapterLocation);
        autoCompletePrice.setAdapter(adapterPrice);
    }

    private void addInn() {
        ServiceAPI.serviceapi.getAllInnsConfirmed().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("innId");
                            String address = jsonObject.getString("address");
                            String phoneNumber = jsonObject.getString("phoneNumber");
                            String describe = jsonObject.getString("describe");
                            Double price = jsonObject.getDouble("price");
                            Double priceWater = jsonObject.getDouble("priceWater");
                            Double priceELec = jsonObject.getDouble("priceELec");
                            String createdAtString = jsonObject.getString("createdAt");
                            String updatedAtString = jsonObject.getString("updatedAt");
                            String proposed = jsonObject.getString("proposed");
                            JSONObject mainImage = jsonObject.getJSONObject("mainImage");
                            Date createdAt = null;
                            Date updatedAt = null;
                            try{
                                createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAtString);
                                updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(updatedAtString);
                            } catch (ParseException e) {
                                Log.e("TAG", e.toString());
                            }

                            ImageInn imageInn = new ImageInn(mainImage.getInt("imageInnId"), mainImage.getString("image"));

                            inns.add(new Inn(id, address, phoneNumber, describe, price, priceWater, priceELec, createdAt, updatedAt, proposed, 0,  imageInn, null));
                            fragments.add(InnFragment.newInstance(getApplicationContext(), describe, String.valueOf(price), imageInn.getImage(), String.valueOf(priceWater), String.valueOf(priceELec)));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    innId = inns.get(0).getInnId();
                    des = inns.get(0).getDescribe();
                    innAdapter = new InnAdapter(ListInnActivity.this, fragments);
                    viewListInn.setAdapter(innAdapter);
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getApplicationContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchInn() {
        String location = txtlocation.getText().toString();
        String price = txtprice.getText().toString();

        if (location == getResources().getString(R.string.location)){
            Toast.makeText(getApplicationContext(), "Vui long chon dia diem va muc gia", Toast.LENGTH_SHORT).show();
            return;
        }
        if (price == getResources().getString(R.string.price)){
            Toast.makeText(getApplicationContext(), "Vui long chon dia diem va muc gia", Toast.LENGTH_SHORT).show();
            return;
        }
        ServiceAPI.serviceapi.searchInns(searchText[0].toString().trim(), Double.valueOf(searchText[1].toString()), Double.valueOf(searchText[2].toString()), Integer.valueOf(searchText[3].toString())).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        inns = new ArrayList<>();
                        fragments = new ArrayList<>();
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("innId");
                            String address = jsonObject.getString("address");
                            String phoneNumber = jsonObject.getString("phoneNumber");
                            String describe = jsonObject.getString("describe");
                            Double price = jsonObject.getDouble("price");
                            Double priceWater = jsonObject.getDouble("priceWater");
                            Double priceELec = jsonObject.getDouble("priceELec");
                            String createdAtString = jsonObject.getString("createdAt");
                            String updatedAtString = jsonObject.getString("updatedAt");
                            String proposed = jsonObject.getString("proposed");
                            JSONObject mainImage = jsonObject.getJSONObject("mainImage");
                            Date createdAt = null;
                            Date updatedAt = null;
                            try{
                                createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAtString);
                                updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(updatedAtString);
                            } catch (ParseException e) {
                                Log.e("TAG", e.toString());
                            }

                            ImageInn imageInn = new ImageInn(mainImage.getInt("imageInnId"), mainImage.getString("image"));

                            inns.add(new Inn(id, address, phoneNumber, describe, price, priceWater, priceELec, createdAt, updatedAt, proposed, 0,  imageInn, null));
                            fragments.add(InnFragment.newInstance(getApplicationContext(), describe, String.valueOf(price), imageInn.getImage(), String.valueOf(priceWater), String.valueOf(priceELec)));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    innId = inns.get(0).getInnId();
                    innAdapter = new InnAdapter(ListInnActivity.this, fragments);
                    viewListInn.setAdapter(innAdapter);
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getApplicationContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }
}