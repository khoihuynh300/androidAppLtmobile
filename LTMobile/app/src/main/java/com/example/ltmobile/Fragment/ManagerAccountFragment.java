package com.example.ltmobile.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ltmobile.Activity.AdminAddUserActivity;
import com.example.ltmobile.Adapter.AdminAccountAdapter;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ItemMarginDecoration;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerAccountFragment extends Fragment {
    Context context;

    // final string for dropdown list
    private final String STRING_ASC = "Tăng dần";
    private final String STRING_DESC = "Giảm dần";
    private final String STRING_ACTIVE = "Hoạt động";
    private final String STRING_NOTACTIVE = "Khóa";

    // recycle view
    public static List<User> userList;
    AdminAccountAdapter adminAccountAdapter;
    RecyclerView rvUser;

    //View
    ImageButton btnShowFilter;
    LinearLayout layoutFilter;
    Button btnFilter;
    FloatingActionButton fabAddUser;
    Spinner spinnerArrange, spinnerIsActive;
    EditText inputName;

    //filter
    int offset = 0;
    boolean ascending = true;
    boolean isActive = true;
    String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manager_account, container, false);
        userList = new ArrayList<>();
        connectView(view);
        setUpRecycleView();
        renderData();
        setupSpinnerData();
        setupEvent();
        return view;
    }
    private void connectView(View view){
        rvUser = view.findViewById(R.id.rv_accounts);
        btnShowFilter = view.findViewById(R.id.btnShowFilter);
        layoutFilter = view.findViewById(R.id.layoutFilter);
        btnFilter = view.findViewById(R.id.btnFilter);
        fabAddUser = view.findViewById(R.id.fabAddAccount);
        spinnerArrange = view.findViewById(R.id.spinnerArrange);
        spinnerIsActive = view.findViewById(R.id.spinnerIsActive);
        inputName = view.findViewById(R.id.inputFullname);
    }

    private void setUpRecycleView(){
        adminAccountAdapter = new AdminAccountAdapter(context, userList);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(30);
        rvUser.addItemDecoration(itemMarginDecoration);
        rvUser.setAdapter(adminAccountAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvUser.setLayoutManager(linearLayoutManager);

        rvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutFilter.setVisibility(View.GONE);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager1 != null
                        && linearLayoutManager1.findLastCompletelyVisibleItemPosition() == userList.size() - 1
                        && userList.size() != 0){
                    offset++;
                    renderData();
                }
            }
        });
    }

    private void renderData(){

        ServiceAPI.serviceapi.getUserFilter(offset, ascending, isActive, name)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject responseObject = response.body();
                            if(!responseObject.get("error").getAsBoolean() ) {
                                JsonArray userListJson = responseObject.get("result").getAsJsonArray();
                                for(int i = 0; i < userListJson.size(); i++){
                                    JsonObject userJson = userListJson.get(i).getAsJsonObject();
                                    User user = new User(
                                            userJson.get("userId").getAsInt(),
                                            userJson.get("email").getAsString(),
                                            userJson.get("fullname").getAsString(),
                                            userJson.get("gender").getAsString(),
                                            userJson.get("avatar").getAsString(),
                                            userJson.get("role").getAsString());

                                    userList.add(user);
                                    adminAccountAdapter.notifyDataSetChanged();
                                }
                            }
                            else{
                                // do something when api return error:true
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
        adminAccountAdapter.notifyDataSetChanged();
    }

    private void setupEvent(){
        btnShowFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutFilter.getVisibility() == View.GONE){
                    layoutFilter.setVisibility(View.VISIBLE);
                }
                else {
                    layoutFilter.setVisibility(View.GONE);
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset = 0;
                ascending = spinnerArrange.getSelectedItem().toString().equals(STRING_ASC);
                isActive = spinnerIsActive.getSelectedItem().toString().equals(STRING_ACTIVE);
                name = inputName.getText().toString();

                userList.clear();
                adminAccountAdapter.notifyDataSetChanged();
                renderData();
                adminAccountAdapter.notifyDataSetChanged();
            }
        });

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AdminAddUserActivity.class));
            }
        });
    }

    private void setupSpinnerData(){
        List<String> arrangeList = new ArrayList<String>();
        arrangeList.add(STRING_ASC);
        arrangeList.add(STRING_DESC);

        List<String> activeList = new ArrayList<String>();
        activeList.add(STRING_ACTIVE);
        activeList.add(STRING_NOTACTIVE);

        ArrayAdapter<String> dataAdapterArrange = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrangeList);
        dataAdapterArrange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrange.setAdapter(dataAdapterArrange);

        ArrayAdapter<String> dataAdapterIsDeleted = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, activeList);
        dataAdapterIsDeleted.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsActive.setAdapter(dataAdapterIsDeleted);
    }
}