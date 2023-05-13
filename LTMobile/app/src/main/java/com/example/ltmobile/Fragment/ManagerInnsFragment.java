package com.example.ltmobile.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ltmobile.Adapter.AdminInnAdapter;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ItemMarginDecoration;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    // final string for dropdown list
    private final String STRING_ASC = "Tăng dần";
    private final String STRING_DESC = "Giảm dần";
    private final String STRING_DELETED = "Đã xóa";
    private final String STRING_NOTDELETED = "Chưa xóa";
    private final String STRING_ALL = "Tất cả";
    private final String STRING_CONFIRMED = "Đã xác nhận";
    private final String STRING_NOTCONFIRMED = "Chưa xác nhận";


    // recycle view
    public static List<Inn> innList;
    AdminInnAdapter adminInnAdapter;
    RecyclerView rvInn;

    //View
    ImageButton btnShowFilter;
    LinearLayout layoutFilter;
    Button btnFilter;
    FloatingActionButton fabAddInn;
    Spinner spinnerArrange, spinnerIsDeleted, spinnerIsConfirmed;
    EditText inputAddress;

    //filter
    int offset = 0;
    boolean ascending = true;
    boolean isDeleted = false;
    String Address = "";
    String isConfirmed = "all"; // all, true, false

    // vi tri scroll
    private int scrollPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_manager_inns, container, false);
        innList = new ArrayList<>();
        connectView(view);
        setUpRecycleView();

        renderData();

        setupEvent();
        setupSpinnerData();

        return view;
    }
    private void connectView(View view){
        rvInn = view.findViewById(R.id.rv_inns);
        btnShowFilter = view.findViewById(R.id.btnShowFilter);
        layoutFilter = view.findViewById(R.id.layoutFilter);
        btnFilter = view.findViewById(R.id.btnFilter);
        fabAddInn = view.findViewById(R.id.fabAddInn);

        spinnerArrange = view.findViewById(R.id.spinnerArrange);
        spinnerIsDeleted = view.findViewById(R.id.spinnerIsDeleted);
        spinnerIsConfirmed = view.findViewById(R.id.spinnerIsConfirmed);
        inputAddress = view.findViewById(R.id.inputAddress);

    }
    private void setUpRecycleView(){
        adminInnAdapter = new AdminInnAdapter(context, innList);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(30);
        rvInn.addItemDecoration(itemMarginDecoration);
        rvInn.setAdapter(adminInnAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvInn.setLayoutManager(linearLayoutManager);

        // khi cuộn xuống cuối cùng
        rvInn.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        && linearLayoutManager1.findLastCompletelyVisibleItemPosition() == innList.size() - 1){
                    offset++;
                    renderData();
                }
            }
        });
    }
    private void renderData(){

        ServiceAPI.serviceapi.getInnsFilter(offset, ascending, isDeleted, Address, isConfirmed)
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

                                rvInn.scrollToPosition(scrollPosition);
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
        // lấy vị trí cũ
        scrollPosition = ((LinearLayoutManager) rvInn.getLayoutManager()).findLastVisibleItemPosition() - 1;

        // xóa dữ liệu cũ rồi đặt lại dữ liệu mới
//        innList.clear();
        adminInnAdapter.notifyDataSetChanged();
//        renderData();

        // đặt lại vị trí cũ
        rvInn.scrollToPosition(scrollPosition);
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
                isDeleted = spinnerIsDeleted.getSelectedItem().toString().equals(STRING_DELETED);
                Address = inputAddress.getText().toString();
                String isConfimedString = spinnerIsConfirmed.getSelectedItem().toString();
                if(isConfimedString.equals(STRING_ALL)){
                    isConfirmed = "all";
                }
                else if(isConfimedString.equals(STRING_CONFIRMED)){
                    isConfirmed = "true";
                }
                else if(isConfimedString.equals(STRING_NOTCONFIRMED)){
                    isConfirmed = "false";
                }


                // xóa dữ liệu cũ rồi đặt lại dữ liệu mới
                innList.clear();
                adminInnAdapter.notifyDataSetChanged();
                renderData();
                adminInnAdapter.notifyDataSetChanged();
            }
        });

        fabAddInn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupSpinnerData(){
        List<String> arrange = new ArrayList<String>();
        arrange.add(STRING_ASC);
        arrange.add(STRING_DESC);

        List<String> isDeleted = new ArrayList<String>();
        isDeleted.add(STRING_NOTDELETED);
        isDeleted.add(STRING_DELETED);

        List<String> isConfirmed = new ArrayList<String>();
        isConfirmed.add(STRING_ALL);
        isConfirmed.add(STRING_CONFIRMED);
        isConfirmed.add(STRING_NOTCONFIRMED);

        ArrayAdapter<String> dataAdapterArrange = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrange);
        dataAdapterArrange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrange.setAdapter(dataAdapterArrange);

        ArrayAdapter<String> dataAdapterIsDeleted = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, isDeleted);
        dataAdapterIsDeleted.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsDeleted.setAdapter(dataAdapterIsDeleted);

        ArrayAdapter<String> dataAdapterIsConfirmed = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, isConfirmed);
        dataAdapterIsConfirmed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsConfirmed.setAdapter(dataAdapterIsConfirmed);

    }
}