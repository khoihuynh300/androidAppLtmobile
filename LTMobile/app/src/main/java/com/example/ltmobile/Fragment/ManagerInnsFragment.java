package com.example.ltmobile.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ltmobile.Adapter.AdminInnAdapter;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ItemMarginDecoration;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerInnsFragment extends Fragment {
    Context context;
    List<Inn> innList;

    AdminInnAdapter adminInnAdapter;
    RecyclerView rvInn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_manager_inns, container, false);
        innList = new ArrayList<>();
        connectView(view);
//        renderData();

        setUpRecycleView();

        return view;
    }
    private void connectView(View view){
        rvInn = view.findViewById(R.id.rv_inns);
    }
    private void setUpRecycleView(){
        adminInnAdapter = new AdminInnAdapter(context, innList);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(30);
        rvInn.addItemDecoration(itemMarginDecoration);
        rvInn.setAdapter(adminInnAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvInn.setLayoutManager(linearLayoutManager);

        // scroll listener
//        rvInn.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                if (linearLayoutManager1 != null
//                        && linearLayoutManager1.findLastCompletelyVisibleItemPosition() == innList.size() - 1){
//                    renderData();
//                }
//            }
//        });
    }
    private void renderData(){
        ServiceAPI.serviceapi.getInns()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject responseObject = response.body();
                            if(!responseObject.get("error").getAsBoolean() ){
                                JsonArray innListJson = responseObject.get("result").getAsJsonArray();

                                for(int i = 0; i < innListJson.size(); i++){
                                    JsonObject innJson = innListJson.get(i).getAsJsonObject();
                                    Date createdAt = null;
                                    Date updatedAt = null;
                                    try{
                                        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(innJson.get("createdAt").getAsString());
                                        updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(innJson.get("updatedAt").getAsString());
                                    } catch (ParseException e) {
                                        Log.e("TAG", e.toString());
                                    }

                                    JsonObject mainImageJson = innJson.get("mainImage").getAsJsonObject();
                                    ImageInn mainImage = new ImageInn(mainImageJson.get("imageInnId").getAsInt(), mainImageJson.get("image").getAsString());

                                    List<ImageInn> ImageInnList = new ArrayList<>();
                                    JsonArray imageListJson = innJson.get("images").getAsJsonArray();
                                    for(int j = 0; j < imageListJson.size(); j++){
                                        JsonObject imageJson = imageListJson.get(j).getAsJsonObject();
                                        ImageInn imageInn = new ImageInn(imageJson.get("imageInnId").getAsInt(), imageJson.get("image").getAsString());
                                        ImageInnList.add(imageInn);
                                    }
                                    Inn inn = new Inn(
                                            innJson.get("innId").getAsInt(),
                                            innJson.get("address").getAsString(),
                                            innJson.get("phoneNumber").getAsString(),
                                            innJson.get("describe").getAsString(),
                                            innJson.get("price").getAsDouble(),
                                            innJson.get("priceWater").getAsDouble(),
                                            innJson.get("priceELec").getAsDouble(),
                                            createdAt,
                                            updatedAt,
                                            "",
                                            innJson.get("proposedId").getAsInt(),
                                            mainImage,
                                            ImageInnList
                                            );
                                    inn.setConfirmed(innJson.get("isConfirmed").getAsBoolean());
                                    innList.add(inn);
                                    adminInnAdapter.notifyDataSetChanged();
//                                    adminInnAdapter.addItem(inn);
                                }
                            }


                        }
                        else {
                            Toast.makeText(context, "an error occur", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("TAG", t.toString());
                        Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        innList.clear();
        adminInnAdapter.notifyDataSetChanged();
        renderData();
    }
}