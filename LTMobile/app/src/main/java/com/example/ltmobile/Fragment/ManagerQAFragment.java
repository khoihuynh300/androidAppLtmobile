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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ltmobile.Adapter.AdminAccountAdapter;
import com.example.ltmobile.Adapter.QuestionAdapter;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ItemMarginDecoration;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;

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

public class ManagerQAFragment extends Fragment {

    Context context;
    // final string for dropdown list
    private final String STRING_ASC = "Tăng dần";
    private final String STRING_DESC = "Giảm dần";
    private final String STRING_DELETED = "true";
    private final String STRING_NOTDELETED = "false";

    // recycle view
    public static List<Question> questionList;
    QuestionAdapter questionAdapter;
    RecyclerView rv_Questions;

    //View
    ImageButton btnShowFilter;
    LinearLayout layoutFilter;
    Button btnFilter;
    Spinner spinnerArrange, spinnerIsDeleted;
    EditText inputName;

    //filter
    int offset = 0;
    boolean ascending = true;
    boolean isDeleted = false;
    String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_q_a, container, false);
        questionList = new ArrayList<>();
        connectView(view);
        setUpRecycleView();
        renderData();
        setupSpinnerData();
        setupEvent();
        return view;
    }

    private void connectView(View view){
        rv_Questions = view.findViewById(R.id.rv_Questions);
        btnShowFilter = view.findViewById(R.id.btnShowFilter);
        layoutFilter = view.findViewById(R.id.layoutFilter);
        btnFilter = view.findViewById(R.id.btnFilter);
        spinnerArrange = view.findViewById(R.id.spinnerArrange);
        spinnerIsDeleted = view.findViewById(R.id.spinnerIsDeleted);
        inputName = view.findViewById(R.id.inputFullname);
    }
    private void setUpRecycleView(){
        questionAdapter = new QuestionAdapter(context, questionList);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(30);
        rv_Questions.addItemDecoration(itemMarginDecoration);
        rv_Questions.setAdapter(questionAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_Questions.setLayoutManager(linearLayoutManager);

        rv_Questions.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        && linearLayoutManager1.findLastCompletelyVisibleItemPosition() == questionList.size() - 1
                        && questionList.size() != 0){
                    offset++;
//                    renderData();
                }
            }
        });
    }

    private void setupSpinnerData(){
        List<String> arrangeList = new ArrayList<String>();
        arrangeList.add(STRING_ASC);
        arrangeList.add(STRING_DESC);

        List<String> activeList = new ArrayList<String>();
        activeList.add(STRING_NOTDELETED);
        activeList.add(STRING_DELETED);

        ArrayAdapter<String> dataAdapterArrange = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrangeList);
        dataAdapterArrange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrange.setAdapter(dataAdapterArrange);

        ArrayAdapter<String> dataAdapterIsDeleted = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, activeList);
        dataAdapterIsDeleted.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsDeleted.setAdapter(dataAdapterIsDeleted);
    }

    private void setupEvent() {
        btnShowFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutFilter.getVisibility() == View.GONE) {
                    layoutFilter.setVisibility(View.VISIBLE);
                } else {
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
                name = inputName.getText().toString();

                questionList.clear();
                questionAdapter.notifyDataSetChanged();
                renderData();
                questionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void renderData(){
        addQuestions();
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

    private void addQuestions() {
        ServiceAPI.serviceapi.getAllQuestions().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("questionId");
                            String title = jsonObject.getString("title");
                            Double view = jsonObject.getDouble("view");
                            String createdAtString = jsonObject.getString("createdAt");
                            String updatedAtString = jsonObject.getString("updatedAt");
                            JSONObject askedUser = jsonObject.getJSONObject("askedId");
                            int answererId = 0;
                            String avatarAnswered = "";
                            String answeredFullname = "";
                            if (!jsonObject.isNull("answererId")) {
                                JSONObject answererUser = jsonObject.getJSONObject("answererId");
                                answererId = answererUser.getInt("userId");
                                answeredFullname = askedUser.getString("fullname");
                                String roleAnswered = answererUser.getString("role");
                                avatarAnswered = answererUser.getString("avatar");
                            }
                            String avatarAsked = "";
                            int askedId = askedUser.getInt("userId");
                            String askedFullname = askedUser.getString("fullname");
                            String roleAsked = askedUser.getString("role");
                            avatarAsked = askedUser.getString("avatar");
                            Date createdAt = null;
                            Date updatedAt = null;
                            try {
                                createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAtString);
                                updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(updatedAtString);
                            } catch (ParseException e) {
                                Log.e("TAG", e.toString());
                            }
                            Log.e("TAG", "onResponse: " + avatarAsked );
                            questionList.add(new Question(id, createdAt, updatedAt, title, avatarAsked, askedId, answererId, askedFullname, answeredFullname));

                            questionAdapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }
}