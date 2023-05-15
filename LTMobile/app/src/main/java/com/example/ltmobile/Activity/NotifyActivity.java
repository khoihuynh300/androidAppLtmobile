package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ltmobile.Adapter.NotifyAdapter;
import com.example.ltmobile.Model.Notify;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ItemMarginDecoration;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyActivity extends AppCompatActivity {
    Context context = this;

    RecyclerView rv_notify;
    NotifyAdapter notifyAdapter;
    List<Notify> notifyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_notify = findViewById(R.id.rv_notify);
        notifyList = new ArrayList<>();

        notifyAdapter = new NotifyAdapter(context, notifyList);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(30);
        rv_notify.addItemDecoration(itemMarginDecoration);
        rv_notify.setAdapter(notifyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_notify.setLayoutManager(linearLayoutManager);

        ServiceAPI.serviceapi.getAllNotifyByUser(SharedPrefManager.getInstance(context).getUser().getUserId())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()) {
                            JsonObject responseObject = response.body();
                            if (!responseObject.get("error").getAsBoolean()) {
                                JsonArray innListJson = responseObject.get("result").getAsJsonArray();

                                Log.e("TAG", "onResponse: " + innListJson);

                                for(int i = 0; i < innListJson.size(); i++) {
                                    JsonObject innJson = innListJson.get(i).getAsJsonObject();
                                    Notify notify = new Notify(
                                            innJson.get("notificationId").getAsInt(),
                                            innJson.get("notificationContent").getAsString(),
                                            innJson.get("notificationLink").getAsString(),
                                            innJson.get("isViewed").getAsBoolean());

                                    notifyList.add(notify);
                                    notifyAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }
}